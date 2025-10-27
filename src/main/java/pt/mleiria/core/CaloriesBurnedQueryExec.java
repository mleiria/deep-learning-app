package pt.mleiria.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.vo.CaloriesBurnedVo;
import pt.mleiria.vo.HeartRateVo;
import tech.tablesaw.api.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static pt.mleiria.vo.DataMapper.caloriesBurnedVoListTableFunction;
import static pt.mleiria.vo.DataMapper.heartRateVoListTableFunction;

public class CaloriesBurnedQueryExec implements QueryExec {

    private static final Logger logger = LoggerFactory.getLogger(CaloriesBurnedQueryExec.class);


    @Override
    public Table execQueryForJson(String sql) {
        try (final Connection conn = ds.getConnection();
             final PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // CRITICAL PERFORMANCE TWEAK
            pstmt.setFetchSize(1000);
            final List<CaloriesBurnedVo> caloriesBurnedVos = new ArrayList<>();
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    JacksonUtils.optionalDecode(rs.getString(1), CaloriesBurnedVo.class).ifPresent(caloriesBurnedVos::add);
                }
            }
            return caloriesBurnedVoListTableFunction.apply(caloriesBurnedVos);
        } catch (SQLException e) {
            logger.error("Failed to execute query and create table", e);
            return Table.create("Error Table");
        }
    }

    public Table execQueryForJson() {
        final String sql = "select uuid, data from calories_burned_details where (data ->> 'activityList') IS NOT NULL";
        try (final Connection conn = ds.getConnection();
             final PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // CRITICAL PERFORMANCE TWEAK
            pstmt.setFetchSize(1000);
            final List<CaloriesBurnedVo> caloriesBurnedVos = new ArrayList<>();
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    final String uuid = rs.getString(1);
                    JacksonUtils.optionalDecode(rs.getString(2), CaloriesBurnedVo.class).ifPresent(
                            caloriesBurnedVo -> {
                                caloriesBurnedVo.setUuid(uuid);
                            caloriesBurnedVos.add(caloriesBurnedVo);
                            }
                    );
                }
            }
            return caloriesBurnedVoListTableFunction.apply(caloriesBurnedVos);
        } catch (SQLException e) {
            logger.error("Failed to execute query and create table", e);
            return Table.create("Error Table");
        }
    }


}
