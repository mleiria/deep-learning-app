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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public interface GenericProcessor {

    Logger logger = LoggerFactory.getLogger(GenericProcessor.class);

    void processJsonFilesInFolder(final DataSource ds);

    Function<String, String> extractUUIDFromFileName = (String path) -> {
        if (path == null || path.isEmpty()) {
            return "";
        }
        int lastSeparatorIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        if (lastSeparatorIndex == -1) {
            return path; // No directory part, return the whole string
        }
        return path.substring(lastSeparatorIndex + 1).substring(0, 36);
    };

    default void processDirectory(Path startPath, DataSource ds, Set<String> existingData,
                                        BiFunction<Path, Set<String>, Stream<JsonDocument>> pathToJsonDocumentsFunc,
                                        DataLocation dataLocation) {
        List<JsonDocument> documentsToInsert;

        // STEP 1: CPU-Bound Parallel Processing
        try (Stream<Path> paths = Files.walk(startPath)) {
            documentsToInsert = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".json"))
                    .parallel() // Best placement for this part of the job
                    .flatMap(elem -> pathToJsonDocumentsFunc.apply(elem, existingData))
                    .toList(); // Collect results instead of acting on them
        } catch (IOException e) {
            logger.error("Error walking through the directory: {}", e.getMessage());
            return; // Exit if we can't even walk the directory
        }

        // STEP 2: I/O-Bound Database Insertion
        logger.info("Finished processing files. Found {} new documents to insert.", documentsToInsert.size());

        // Now you can insert them sequentially, in batches, or using a separate ExecutorService
        // for controlled concurrency against the database.
        final int count = insertJsonDocumentsInBatch(ds, dataLocation.getTableName(), documentsToInsert);

        logger.info("Database insertion complete with {} new records added.", count);
    }

     /**
     * A generic, reusable method to process a JSON file into a stream of JsonDocument objects.
     *
     * @param path           The path to the JSON file.
     * @param existingData   A set of unique strings representing data that should be filtered out.
     * @param typeRef        A Jackson TypeReference for deserializing the JSON into a List<T>.
     * @param toUniqueString A function that converts an object of type T into a unique string for the filter check.
     * @param <T>            The generic type of the objects in the JSON list.
     * @return A Stream of JsonDocument objects, or an empty stream if an error occurs.
     */
     default <T> Stream<JsonDocument> pathToJsonDocumentsGeneric(
            final Path path,
            final Set<String> existingData,
            final TypeReference<List<T>> typeRef,
            final Function<T, String> toUniqueString) {
        try {
            final String fileContent = Files.readString(path);
            // 1. Use the passed-in TypeReference for decoding
            final List<T> jsonObjects = JacksonUtils.decode(fileContent, typeRef);

            return jsonObjects.stream()
                    // 2. Use the passed-in function for the filter condition
                    .filter(elem -> !existingData.contains(toUniqueString.apply(elem)))
                    // The rest of the stream is generic and doesn't need to change
                    .map(JacksonUtils::optionalEncode)
                    .flatMap(Optional::stream)
                    .map(jsonString -> new JsonDocument(path, jsonString));

        } catch (IOException e) {
            logger.error("Error reading or processing file {}: {}", path, e.getMessage());
            return Stream.empty();
        }
    }

    default Set<String> listJsonData(DataSource ds, final String tableName) {
        final String sql = "SELECT data FROM " + tableName;
        try (final Connection conn = ds.getConnection()) {
            final PreparedStatement pstmt = conn.prepareStatement(sql);
            var rs = pstmt.executeQuery();
            final Set<String> jsonData = new HashSet<>();
            while (rs.next()) {
                jsonData.add(rs.getString("data"));
            }
            return jsonData;
        } catch (SQLException e) {
            logger.error("Failed to list data from {}: {}", tableName, e.getMessage());
            return Collections.emptySet();
        }
    }

    default Set<String> listJsonFilePaths(DataSource ds, final String tableName) {
        final String sql = "SELECT file_path FROM " + tableName;
        try (final Connection conn = ds.getConnection()) {
            final PreparedStatement pstmt = conn.prepareStatement(sql);
            var rs = pstmt.executeQuery();
            final Set<String> jsonData = new HashSet<>();
            while (rs.next()) {
                jsonData.add(rs.getString("file_path"));
            }
            return jsonData;
        } catch (SQLException e) {
            logger.error("Failed to list data from {}: {}", tableName, e.getMessage());
            return Collections.emptySet();
        }
    }

    default void insertJsonDocument(final DataSource ds, final String tableName, String filePath, String jsonContent) {

        // The SQL INSERT statement with a cast to the jsonb type.
        final String sql = "INSERT INTO " + tableName + "(file_path, uuid, data) VALUES (?, ?::jsonb)";
        //logger.info(() -> "Trying to insert into " + tableName + " the file: " + filePath + " with content: " + jsonContent);
        try (final Connection conn = ds.getConnection()) {
            final PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, filePath);
            pstmt.setString(2, extractUUIDFromFileName.apply(filePath));
            pstmt.setString(3, jsonContent);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to insert {}: {}", jsonContent, e.getMessage());
        }
    }

    default int insertJsonDocumentsInBatch(final DataSource ds, final String tableName, final List<JsonDocument> documents) {

         final Set<String> existingFilePaths = listJsonFilePaths(ds, tableName);
         final List<JsonDocument> newDocuments = documents.stream()
                 .filter(doc -> !existingFilePaths.contains(doc.sourcePath().toString()))
                 .toList();


        final String sql = "INSERT INTO " + tableName + "(file_path, uuid, data) VALUES (?, ?, ?::jsonb)";
        final int BATCH_SIZE = 1000; // A good starting point for batch size
        int count = 0;
        try (final Connection conn = ds.getConnection();
             final PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (final JsonDocument doc : newDocuments) {
                final String pathToFile = doc.sourcePath().toString();
                pstmt.setString(1, pathToFile);
                pstmt.setString(2, extractUUIDFromFileName.apply(pathToFile));
                pstmt.setString(3, doc.jsonContent());
                pstmt.addBatch();

                if (++count % BATCH_SIZE == 0) {
                    pstmt.executeBatch(); // Execute the batch
                    logger.info("Executed batch of " + BATCH_SIZE);
                }
            }
            pstmt.executeBatch(); // Execute the final remaining batch
            logger.info("Executed final batch.");

        } catch (SQLException e) {
            // Handle batch update exceptions
            logger.error("Error during batch insert: {}", e.getMessage());
        }
        return count;
    }
}
