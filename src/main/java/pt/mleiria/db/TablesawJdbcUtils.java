package pt.mleiria.db;

import tech.tablesaw.api.*;
import tech.tablesaw.columns.Column;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class TablesawJdbcUtils {

    public static Table fromResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // 1. Create a list of empty, typed columns based on metadata
        List<Column<?>> columns = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            columns.add(createColumn(metaData.getColumnName(i), metaData.getColumnType(i)));
        }

        // 2. Iterate and append, handling NULLs
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                Object obj = rs.getObject(i);
                if (obj == null) {
                    columns.get(i - 1).appendMissing();
                } else {
                    // This part can be tricky. A simple approach:
                    columns.get(i - 1).appendObj(obj);
                }
            }
        }
        return Table.create("Query Result", columns);
    }

    // Helper to map JDBC types to Tablesaw column types
    private static Column<?> createColumn(String name, int jdbcType) {
        switch (jdbcType) {
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.LONGVARCHAR:
                return StringColumn.create(name);
            case Types.INTEGER:
            case Types.TINYINT:
            case Types.SMALLINT:
                return IntColumn.create(name);
            case Types.BIGINT:
                return LongColumn.create(name);
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.DOUBLE:
            case Types.REAL:
                return DoubleColumn.create(name);
            case Types.FLOAT:
                return FloatColumn.create(name);
            case Types.BOOLEAN:
            case Types.BIT:
                return BooleanColumn.create(name);
            case Types.DATE:
                return DateColumn.create(name);
            case Types.TIME:
                return TimeColumn.create(name);
            case Types.TIMESTAMP:
                return DateTimeColumn.create(name);
            default:
                // Fallback to StringColumn for unsupported types
                return StringColumn.create(name);
        }
    }


}
