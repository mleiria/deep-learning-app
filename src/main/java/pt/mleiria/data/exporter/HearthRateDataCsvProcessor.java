package pt.mleiria.data.exporter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.config.ConfigLoader;
import pt.mleiria.core.StopWatch;
import pt.mleiria.data.GenericCsvProcessor;
import pt.mleiria.db.DataSourceFactory;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class HearthRateDataCsvProcessor implements GenericCsvProcessor {
    private static final Logger logger = LoggerFactory.getLogger(HearthRateDataCsvProcessor.class);

    private final DataSource ds;

    public HearthRateDataCsvProcessor() {
        this.ds = DataSourceFactory.getDataSource();

    }

    @Override
    public void writeDataToCsv(final String outputFilePath) {
        final StopWatch stopWatch = new StopWatch();
        // Define the headers for your CSV file
        final String sql = ConfigLoader.INSTANCE.selectAllRawHeartRate();
        logger.info("Executing SQL: {}", sql);
        final String[] headers = {
                "start_timestamp", "end_timestamp", "heart_rate", "heart_rate_min", "heart_rate_max"
        };

        // Use a nested try-with-resources block to manage all resources automatically.
        // This is the cleanest and safest way.
        try (
                // Database resources
                final Connection conn = ds.getConnection();
                final PreparedStatement pstmt = conn.prepareStatement(sql);
                final ResultSet rs = pstmt.executeQuery();

                // File I/O resources
                final BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath));
                final CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.Builder.create(CSVFormat.DEFAULT).setHeader(headers).build());
        ) {
            // Loop through the streaming result set
            while (rs.next()) {
                // Write one record at a time to the CSV file
                csvPrinter.printRecord(
                        rs.getString("start_timestamp"),
                        rs.getString("end_timestamp"),
                        rs.getDouble("heart_rate"),
                        rs.getDouble("heart_rate_min"),
                        rs.getDouble("heart_rate_max")
                );
            }

            csvPrinter.flush(); // Ensure all buffered data is written to the file
            logger.info("CSV file was created successfully at: {}", outputFilePath);

        } catch (Exception e) { // Catch a broader exception for IO and SQL
            logger.error("Error while writing data to CSV", e);
        }
        logger.info("Elapsed time to write CSV: {} secs", stopWatch.stop());
    }
}
