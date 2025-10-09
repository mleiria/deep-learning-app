package pt.mleiria.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import pt.mleiria.data.importer.config.Config;

import javax.sql.DataSource;

public class DataSourceFactory {

    private static HikariDataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(Config.DB_URL_REMOTE.getValue());
            config.setUsername(Config.USER.getValue());
            config.setPassword(Config.PASSWORD.getValue());
            // Tuning: Set pool size based on your DB server's capacity
            config.setMaximumPoolSize(10);
            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }
}
