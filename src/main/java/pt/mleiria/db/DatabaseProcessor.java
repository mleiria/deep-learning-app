package pt.mleiria.db;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.core.StopWatch;
import pt.mleiria.data.importer.config.DataLocation;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.StringReader;
import java.sql.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class DatabaseProcessor {

    /**
     * Logger for logging messages and errors.
     */
    private static final Logger logger = LoggerFactory.getLogger(DatabaseProcessor.class);

    /**
     * Extracts a UUID from a file name string.
     */
    private static final Function<String, String> extractUUIDFromFileName = (String path) -> {
        if (path == null || path.isEmpty()) {
            return "";
        }
        int lastSeparatorIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        if (lastSeparatorIndex == -1) {
            return path; // No directory part, return the whole string
        }
        return path.substring(lastSeparatorIndex + 1).substring(0, 36);
    };

    /**
     * Lists all JSON data from the specified table in the database.
     *
     * @param ds        the DataSource for database connections
     * @param tableName the name of the table
     * @return a Set of JSON data strings
     */
    public static Set<String> listJsonData(DataSource ds, final String tableName) {
        return listJson(ds, tableName, "data");
    }

    /**
     * Lists all file paths from the specified table in the database.
     *
     * @param ds        the DataSource for database connections
     * @param tableName the name of the table
     * @return a Set of file path strings
     */
    public static Set<String> listDistinctJsonFilePaths(DataSource ds, final String tableName) {
        return listDistinctJson(ds, tableName, "file_path");
    }
    /**
     * Lists all entries from a specified row in the specified table in the database.
     *
     * @param ds        the DataSource for database connections
     * @param tableName the name of the table
     * @param rowName   the name of the row to retrieve data from
     * @return a Set of strings from the specified row
     */
    public static Set<String> listJson(DataSource ds, final String tableName, final String rowName) {
        final String sql = "SELECT " + rowName + " FROM " + tableName;
        logger.info("Executing SQL: {}", sql);
        try (final Connection conn = ds.getConnection()) {
            final PreparedStatement pstmt = conn.prepareStatement(sql);
            var rs = pstmt.executeQuery();
            final Set<String> jsonData = new HashSet<>();
            while (rs.next()) {
                jsonData.add(rs.getString(rowName));
            }
            return jsonData;
        } catch (SQLException e) {
            logger.error("Failed to list data from {}: {}", tableName, e.getMessage());
            return Collections.emptySet();
        }
    }
    /**
     * Lists all distinct entries from a specified row in the specified table in the database.
     *
     * @param ds        the DataSource for database connections
     * @param tableName the name of the table
     * @param rowName   the name of the row to retrieve data from
     * @return a Set of distinct strings from the specified row
     */
    public static Set<String> listDistinctJson(DataSource ds, final String tableName, final String rowName) {
        final String sql = "SELECT DISTINCT(" + rowName + ") FROM " + tableName;
        logger.info("Executing SQL: {}", sql);
        try (final Connection conn = ds.getConnection();
            final PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setFetchSize(1000); // Adjust fetch size as needed

            var rs = pstmt.executeQuery();
            final Set<String> jsonData = new HashSet<>();
            while (rs.next()) {
                jsonData.add(rs.getString(rowName));
            }
            return jsonData;
        } catch (SQLException e) {
            logger.error("Failed to list data from {}: {}", tableName, e.getMessage());
            return Collections.emptySet();
        }
    }

    /**
     * Inserts a single JSON document into the specified table in the database.
     *
     * @param ds          the DataSource for database connections
     * @param tableName   the name of the table
     * @param filePath    the file path of the JSON document
     * @param jsonContent the JSON content to insert
     */
    public static void insertJsonDocument(final DataSource ds, final String tableName, String filePath, String jsonContent) {

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

    /**
     * Inserts a batch of JSON documents into the specified table in the database.
     *
     * @param ds        the DataSource for database connections
     * @param tableName the name of the table
     * @param documents the list of JsonDocument objects to insert
     * @return the number of records inserted
     */
    public static int insertJsonDocumentsInBatch(final DataSource ds, final DataLocation tableName, final List<JsonDocument> documents) {
        final StopWatch sw = new StopWatch();
        final Set<String> existingFilePaths = listDistinctJsonFilePaths(ds, tableName.getTableName());
        final List<JsonDocument> newDocuments = documents.stream()
                .filter(doc -> !existingFilePaths.contains(doc.sourcePath().toString()))
                .toList();
        logger.info("Found {} new documents to insert after filtering existing file paths. Time taken: {} secs", newDocuments.size(), sw.stop());
        if(newDocuments.isEmpty()) {
            return 0;
        }
        final String sql = "INSERT INTO " + tableName.getTableName() + "(file_path, uuid, data) VALUES (?, ?, ?::jsonb)";
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

    public static List<JsonDocument> findNewDocuments(DataSource ds, String tableName, List<JsonDocument> candidateDocuments) {
        final String tempTableName = "new_paths_temp_" + System.currentTimeMillis();
        final String createTempTableSql = "CREATE TEMPORARY TABLE " + tempTableName + " (file_path TEXT PRIMARY KEY) ON COMMIT DROP";

        // This is the main try block for the connection
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);

            // --- THE FIX IS HERE ---
            // We need the raw PostgreSQL connection to use the CopyManager.
            // The connection from a pool is a proxy, so we must unwrap it.
            BaseConnection pgConnection = conn.unwrap(BaseConnection.class);
            // -----------------------

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTempTableSql);
            }

            // Now use the unwrapped connection with the CopyManager
            CopyManager copyManager = new CopyManager(pgConnection);
            StringBuilder sb = new StringBuilder();
            for (JsonDocument doc : candidateDocuments) {
                sb.append(doc.sourcePath().toString()).append("\n");
            }

            try (StringReader reader = new StringReader(sb.toString())) {
                copyManager.copyIn("COPY " + tempTableName + " (file_path) FROM STDIN", reader);
            }

            final String findNewPathsSql = "SELECT t.file_path FROM " + tempTableName + " t " +
                    "LEFT JOIN " + tableName + " m ON t.file_path = m.file_path " +
                    "WHERE m.file_path IS NULL";

            Set<String> newFilePaths;
            try (PreparedStatement pstmt = conn.prepareStatement(findNewPathsSql);
                 ResultSet rs = pstmt.executeQuery()) {

                newFilePaths = new HashSet<>();
                while (rs.next()) {
                    newFilePaths.add(rs.getString(1));
                }
            }

            conn.commit();

            return candidateDocuments.stream()
                    .filter(doc -> newFilePaths.contains(doc.sourcePath().toString()))
                    .toList();

        } catch (SQLException | IOException e) {
            // It's good practice to also log the exception's stack trace for debugging
            logger.error("Failed to find new documents using temp table strategy: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
