package pt.mleiria;

import pt.mleiria.db.DataSourceFactory;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static pt.mleiria.db.TablesawJdbcUtils.fromResultSet;

public class App {

    public static void main(String[] args) {
        final String query = "SELECT id, data ->> 'heart_rate' AS heart_rate, data ->> 'end_time' AS end_time FROM heart_rate;";
        System.out.println(executeQuery(DataSourceFactory.getDataSource(), query));
    }

    public static Table executeQuery(DataSource ds, String sql) {
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // CRITICAL PERFORMANCE TWEAK
            pstmt.setFetchSize(1000);

            try (ResultSet rs = pstmt.executeQuery()) {
                return fromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query and create table", e);
        }
    }

}
