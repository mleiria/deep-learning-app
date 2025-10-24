package pt.mleiria.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import pt.mleiria.config.ConfigLoader;

import javax.sql.DataSource;
import java.util.Properties;

public class DataSourceFactory {

    private static HikariDataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            final HikariConfig config = new HikariConfig();
            config.setJdbcUrl(ConfigLoader.INSTANCE.getDbUrlLocal());
            config.setUsername(ConfigLoader.INSTANCE.getDbUser());
            config.setPassword(ConfigLoader.INSTANCE.getDbPassword());
            // Tuning: Set pool size based on your DB server's capacity
            config.setMaximumPoolSize(10);
            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }
}
