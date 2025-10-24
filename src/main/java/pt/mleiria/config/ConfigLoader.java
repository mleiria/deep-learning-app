package pt.mleiria.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.core.Validator;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public enum ConfigLoader {

    INSTANCE;

    private final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);

    private final Properties prop;
    private final String ext = ".properties";
    private final List<String> propFiles = List.of("config", "heartRateSql");

    ConfigLoader() {
        prop = new Properties();
        // Use the ClassLoader to find the file in the classpath
        propFiles.stream().map(elem -> elem + ext).forEach(propFile -> {
            try (final InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(propFile)) {
                if (Validator.isNull(input)) {
                    logger.error("Sorry, unable to find {} on the classpath.", propFile);
                }
                prop.load(input);
            } catch (IOException ex) {
                logger.error("Sorry, unable to load {} on the classpath.", propFile, ex);
            }
        });
    }
    // DB Configurations

    public String getDbUrlRemote() {
        return prop.getProperty("db.url.remote");
    }

    public String getDbUrlLocal() {
        return prop.getProperty("db.url.local");
    }

    public String getDbUser() {
        return prop.getProperty("db.user");
    }

    public String getDbPassword() {
        return prop.getProperty("db.pwd");
    }

    public String getDataFolder() {
        return prop.getProperty("data.folder");
    }

    public String getDataFolderCsv() {
        return prop.getProperty("data.folder.csv");
    }

    // Query configurations

    public String selectAllHeartRate() {
        return prop.getProperty("select.all");
    }

    public String selectAllRawHeartRate() {
        return prop.getProperty("select.all.raw");
    }
}
