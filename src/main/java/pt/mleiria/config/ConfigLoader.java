package pt.mleiria.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.core.Validator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);

    public static Properties loadProperties(final String propertiesFileName) {
        final Properties prop = new Properties();
        // Use the ClassLoader to find the file in the classpath
        try (final InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (Validator.isNull(input)) {
                logger.error("Sorry, unable to find {} on the classpath.", propertiesFileName);
                return null; // or throw an exception
            }
            // Load the properties from the InputStream
            prop.load(input);
        } catch (IOException ex) {
            logger.error("Sorry, unable to load {} on the classpath.", propertiesFileName, ex);
        }
        return prop;
    }

}
