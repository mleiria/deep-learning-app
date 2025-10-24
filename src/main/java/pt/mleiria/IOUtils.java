package pt.mleiria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.db.DatabaseProcessor;
import tech.tablesaw.api.Table;

import java.io.IOException;

public class IOUtils {

    private static final Logger logger = LoggerFactory.getLogger(IOUtils.class);

    public static void save(final Table tableToSave, final String outputFilePath) {
        try {
            tableToSave.write().csv(outputFilePath);
            logger.info("Successfully saved table to {}", outputFilePath);
        } catch (IOException e) {
            logger.error("Error writing CSV file: {}", e.getMessage(), e);
        }
    }
}
