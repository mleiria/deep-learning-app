package pt.mleiria.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.db.DataSourceFactory;
import tech.tablesaw.api.Table;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static pt.mleiria.db.TablesawJdbcUtils.fromResultSet;

public interface QueryExec {
    Logger logger = LoggerFactory.getLogger(QueryExec.class);

    DataSource ds = DataSourceFactory.getDataSource();

    Table execQueryForJson(final String sql);
    Table execQueryForJson();

    default Table execQuery(final String sql){
        try (final Connection conn = ds.getConnection();
             final PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // CRITICAL PERFORMANCE TWEAK
            pstmt.setFetchSize(1000);

            try (ResultSet rs = pstmt.executeQuery()) {
                return fromResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Failed to execute query and create table", e);
            return Table.create("Error Table");
        }
    }
}
