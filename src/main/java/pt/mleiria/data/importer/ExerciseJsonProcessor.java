package pt.mleiria.data.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.core.StopWatch;
import pt.mleiria.data.importer.config.DataLocation;
import pt.mleiria.db.JsonDocument;
import pt.mleiria.vo.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static pt.mleiria.db.DatabaseProcessor.insertJsonDocumentsInBatch;

public class ExerciseJsonProcessor implements GenericJsonProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ExerciseJsonProcessor.class);


    private final Function<Path, Stream<JsonDocument>> exLiveDataPathToJsonDocuments = this::exLiveDataPathToJsonDocuments;
    private final Function<Path, Stream<JsonDocument>> exLiveDataIntPathToJsonDocuments = this::exLiveDataIntPathToJsonDocuments;
    private final Function<Path, Stream<JsonDocument>> exLocationPathToJsonDocuments = this::exLocationPathToJsonDocuments;
    private final Function<Path, Stream<JsonDocument>> exLocationIntPathToJsonDocuments = this::exLocationIntPathToJsonDocuments;
    private final Function<Path, Stream<JsonDocument>> senseStatusPathToJsonDocuments = this::senseStatusPathToJsonDocuments;
    private final Function<Path, Stream<JsonDocument>> additionalIntPathToJsonDocuments = this::additionalIntPathToJsonDocuments;

    private Stream<JsonDocument> exLiveDataPathToJsonDocuments(Path path) {
        return pathToJsonDocumentsGeneric(
                path,
                new TypeReference<List<ExerciseLiveData>>() {
                });
    }

    private Stream<JsonDocument> exLiveDataIntPathToJsonDocuments(Path path) {
        return pathToJsonDocumentsGeneric(
                path,
                new TypeReference<List<ExerciseLiveDataInternal>>() {
                });
    }

    private Stream<JsonDocument> exLocationPathToJsonDocuments(Path path) {
        return pathToJsonDocumentsGeneric(
                path,
                new TypeReference<List<ExerciseLocation>>() {
                });
    }

    private Stream<JsonDocument> exLocationIntPathToJsonDocuments(Path path) {
        return pathToJsonDocumentsGeneric(
                path,
                new TypeReference<List<ExerciseLocationInternal>>() {
                });
    }

    private Stream<JsonDocument> senseStatusPathToJsonDocuments(Path path) {
        return pathToJsonDocumentsGeneric(
                path,
                new TypeReference<List<SensingStatus>>() {
                });
    }

    private Stream<JsonDocument> additionalIntPathToJsonDocuments(Path path) {
        return pathToJsonDocumentsGeneric(
                path,
                new TypeReference<List<AdditionalInternalVo>>() {
                });
    }

    @Override
    public void processJsonFilesInFolder(final DataSource ds) {
        final DataLocation dataLocation = DataLocation.EXERCISE;
        final Path startPath = getStartPath(dataLocation);
        final StopWatch sw = new StopWatch();
        logger.info("Starting {} for JSON files...", this.getClass().getName());
        processDirectory(startPath, ds, dataLocation);
        logger.info("Running time: {} secs", sw.stop());
    }

    public void processDirectory(final Path startPath, final DataSource ds, final DataLocation dataLocation) {
        List<JsonDocument> documentsToInsert;

        // STEP 1: CPU-Bound Parallel Processing
        try (Stream<Path> paths = Files.walk(startPath)) {
            documentsToInsert = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".json"))
                    .parallel() // Best placement for this part of the job
                    .flatMap(strPath -> {
                        String fileName = strPath.getFileName().toString().toLowerCase().substring(36); // Extract the file name after the UUID and underscore

                        Stream<JsonDocument> result;
                        if (fileName.contains("live_data_internal")) {
                            result = exLiveDataIntPathToJsonDocuments.apply(strPath);
                        } else if (fileName.contains("live_data")) {
                            result = exLiveDataPathToJsonDocuments.apply(strPath);
                        } else if (fileName.contains("location_data_internal")) {
                            result = exLocationIntPathToJsonDocuments.apply(strPath);
                        } else if (fileName.contains("location_data")) {
                            result = exLocationPathToJsonDocuments.apply(strPath);
                        } else if (fileName.contains("sensing_status")) {
                            result = senseStatusPathToJsonDocuments.apply(strPath);
                        } else if (fileName.contains("additional_internal")) {
                            result = additionalIntPathToJsonDocuments.apply(strPath);
                        } else {
                            logger.warn("Unrecognized file pattern: {}", strPath);
                            result = Stream.empty();
                        }
                        return result;
                    })
                    .toList(); // Collect results instead of acting on them
        } catch (
                IOException e) {
            logger.error("Error walking through the directory: {}", e.getMessage());
            return; // Exit if we can't even walk the directory
        }

        // STEP 2: I/O-Bound Database Insertion
        logger.info("Finished processing files. Found {} new documents to insert.", documentsToInsert.size());

        // Now you can insert them sequentially, in batches, or using a separate ExecutorService
        // for controlled concurrency against the database.
        //final List<JsonDocument> documents = findNewDocuments(ds, dataLocation.getTableName(), documentsToInsert);
        final int count = insertJsonDocumentsInBatch(ds, dataLocation, documentsToInsert);

        logger.info("Database insertion complete with {} new records added.", count);
    }
}
