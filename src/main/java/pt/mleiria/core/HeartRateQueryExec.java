package pt.mleiria.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.config.ConfigLoader;
import pt.mleiria.vo.HeartRateVo;
import tech.tablesaw.aggregate.AggregateFunctions;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.selection.Selection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static pt.mleiria.vo.DataMapper.heartRateVoListTableFunction;

public class HeartRateQueryExec implements QueryExec{

    private static final Logger logger = LoggerFactory.getLogger(HeartRateQueryExec.class);


    /**
     * Selects all records from the heart_rate table and returns them as a Tablesaw Table.
     *
     * @return A Tablesaw Table containing all records from the heart_rate table.
     */
    public Table selectAll() {
        final String sql = ConfigLoader.INSTANCE.selectAllHeartRate();
        logger.info("Executing SQL: {}", sql);
        final StringColumn startTime = StringColumn.create("startTime");
        final StringColumn endTime = StringColumn.create("endTime");
        final DoubleColumn heartRate = DoubleColumn.create("heartRate");
        final DoubleColumn heartRateMin = DoubleColumn.create("heartRateMin");
        final DoubleColumn heartRateMax = DoubleColumn.create("heartRateMax");

        try (final Connection conn = ds.getConnection();
             final PreparedStatement pstmt = conn.prepareStatement(sql);
             final ResultSet rs = pstmt.executeQuery()) {

            // 2. ITERATE: Loop through the streaming result set.
            while (rs.next()) {
                startTime.append(rs.getString("start_timestamp"));
                endTime.append(rs.getString("end_timestamp"));
                heartRate.append(rs.getDouble("heart_rate"));
                heartRateMin.append(rs.getDouble("heart_rate_min"));
                heartRateMax.append(rs.getDouble("heart_rate_max"));
            }
        } catch (SQLException e) {
            logger.error("Error executing SQL: {}", sql, e);
            return Table.create("Error Table");
        }

        // 4. ASSEMBLE: Create the final table from the populated columns.
        return Table.create("Heart Rate", startTime, endTime, heartRate, heartRateMin, heartRateMax);

    }



    /**
     * Executes the given SQL query that returns JSON data and maps it to HeartRateVo objects,
     * then converts the list of HeartRateVo objects into a Tablesaw Table.
     *
     * @param sql The SQL query to execute.
     * @return A Tablesaw Table containing the mapped HeartRateVo data.
     */
    @Override
    public Table execQueryForJson(final String sql) {
        try (final Connection conn = ds.getConnection();
             final PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // CRITICAL PERFORMANCE TWEAK
            pstmt.setFetchSize(1000);
            final List<HeartRateVo> heartRateVos = new ArrayList<>();
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    JacksonUtils.optionalDecode(rs.getString(1), HeartRateVo.class).ifPresent(heartRateVos::add);
                }
            }
            return heartRateVoListTableFunction.apply(heartRateVos);
        } catch (SQLException e) {
            logger.error("Failed to execute query and create table", e);
            return Table.create("Error Table");
        }
    }

    @Override
    public Table execQueryForJson() {
        return execQueryForJson("SELECT data FROM heart_rate");
    }

    public Table execQueryWithAggregation(final ChronoUnit dateAggregation) {
        final String sql = "select data from heart_rate";
        final Table table = execQueryForJson(sql);
        table.removeColumns("endTime");

        final DateTimeColumn group = table.dateTimeColumn("startTime")
                .map(ldt -> ldt.truncatedTo(dateAggregation));
        // The rest of the logic is the same: summarize by this new column
        return table.summarize("heartRate", "heartRateMin", "heartRateMax", AggregateFunctions.mean)
                .by(group).sortAscendingOn("startTime");
    }

    public Table aggregate(final Table table, final ChronoUnit dateAggregation){
        table.removeColumns("endTime");

        final DateTimeColumn group = table.dateTimeColumn("startTime")
                .map(ldt -> ldt.truncatedTo(dateAggregation));
        // The rest of the logic is the same: summarize by this new column
        return table.summarize("heartRate", "heartRateMin", "heartRateMax", AggregateFunctions.mean)
                .by(group).sortAscendingOn("startTime");
    }

    public Table execQueryFilterByDate(final int year, final int month, final int day) {
        final String sql = "select data from heart_rate";
        final Table table = execQueryForJson(sql);
        final LocalDate targetDate = LocalDate.of(year, month, day);
        final Selection selection = table.dateTimeColumn("startTime").date().isEqualTo(targetDate);
        return table.where(selection).sortAscendingOn("startTime");
    }

}
