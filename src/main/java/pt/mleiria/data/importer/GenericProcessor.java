package pt.mleiria.data.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.core.JacksonUtils;
import pt.mleiria.vo.DataLocation;
import pt.mleiria.vo.JsonDocument;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static pt.mleiria.db.DatabaseProcessor.insertJsonDocumentsInBatch;

/**
 * GenericProcessor provides methods for processing JSON files in a directory,
 * extracting data, and inserting it into a database.
 */
public interface GenericProcessor {

    /**
     * Logger for logging messages and errors.
     */
    Logger logger = LoggerFactory.getLogger(GenericProcessor.class);

    Function<String, String> makeJsonArray = jsonContent -> jsonContent.startsWith("[")  ? jsonContent : "[" + jsonContent + "]";

    /**
     * Processes all JSON files in a folder and inserts their contents into the database.
     *
     * @param ds the DataSource for database connections
     */
    void processJsonFilesInFolder(final DataSource ds);



    /**
     * Processes a directory, finds JSON files, decodes them, and inserts their contents into the database.
     *
     * @param startPath               the starting directory path
     * @param ds                      the DataSource for database connections
     * @param pathToJsonDocumentsFunc function to convert a Path to a Stream of JsonDocument
     * @param dataLocation            the DataLocation containing table information
     */
    default void processDirectory(Path startPath, DataSource ds,
                                  Function<Path, Stream<JsonDocument>> pathToJsonDocumentsFunc,
                                  DataLocation dataLocation) {
        List<JsonDocument> documentsToInsert;

        // STEP 1: CPU-Bound Parallel Processing
        try (Stream<Path> paths = Files.walk(startPath)) {
            documentsToInsert = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".json"))
                    .parallel() // Best placement for this part of the job
                    .flatMap(pathToJsonDocumentsFunc)
                    .toList(); // Collect results instead of acting on them
        } catch (IOException e) {
            logger.error("Error walking through the directory: {}", e.getMessage());
            return; // Exit if we can't even walk the directory
        }

        // STEP 2: I/O-Bound Database Insertion
        logger.info("Finished processing files. Found {} new documents to insert.", documentsToInsert.size());

        // Now you can insert them sequentially, in batches, or using a separate ExecutorService
        // for controlled concurrency against the database.
        final int count = insertJsonDocumentsInBatch(ds, dataLocation, documentsToInsert);

        logger.info("Database insertion complete with {} new records added.", count);
    }

    /**
     * Converts a file at the given path to a Stream of JsonDocument using the provided TypeReference.
     *
     * @param path    the path to the JSON file
     * @param typeRef the TypeReference for decoding JSON
     * @param <T>     the type of objects in the JSON file
     * @return a Stream of JsonDocument
     */
    default <T> Stream<JsonDocument> pathToJsonDocumentsGeneric(
            final Path path,
            final TypeReference<List<T>> typeRef) {
        try {
            final String fileContent = Files.readString(path);
            // 1. Use the passed-in TypeReference for decoding
            final List<T> jsonObjects = JacksonUtils.decode(makeJsonArray.apply(fileContent), typeRef);

            return jsonObjects.stream()
                    .map(JacksonUtils::optionalEncode)
                    .flatMap(Optional::stream)
                    .peek(logger::debug)
                    .map(jsonString -> new JsonDocument(path, jsonString));

        } catch (IOException e) {
            logger.error("Error reading or processing file {}: {}", path, e.getMessage());
            return Stream.empty();
        }
    }






}
