package pt.mleiria.vo;

import pt.mleiria.core.DateUtils;
import pt.mleiria.core.Validator;
import tech.tablesaw.api.*;

import java.util.List;
import java.util.function.Function;

public class DataMapper {

    public static final Function<HeartRateVo, Table> heartRateVoTableFunction = heartRateVo -> Table.create("Heart Rate Data")
            .addColumns(
                    LongColumn.create("startTime", heartRateVo.getStartTime()),
                    LongColumn.create("endTime", heartRateVo.getEndTime()),
                    DoubleColumn.create("heartRate", heartRateVo.getHeartRate()),
                    DoubleColumn.create("heartRateMin", heartRateVo.getHeartRateMin()),
                    DoubleColumn.create("heartRateMax", heartRateVo.getHeartRateMax())
            );

    public static final Function<List<HeartRateVo>, Table> heartRateVoListTableFunction = heartRateVoList -> {
        final Table table = Table.create("Heart Rate Data");
        if (Validator.isNotEmptyList(heartRateVoList)) {
            final DateTimeColumn startTimeCol = DateTimeColumn.create("startTime");
            final DateTimeColumn endTimeCol = DateTimeColumn.create("endTime");
            final DoubleColumn heartRateCol = DoubleColumn.create("heartRate");
            final DoubleColumn heartRateMinCol = DoubleColumn.create("heartRateMin");
            final DoubleColumn heartRateMaxCol = DoubleColumn.create("heartRateMax");
            for (final HeartRateVo heartRateVo : heartRateVoList) {
                startTimeCol.append(DateUtils.convertTimestampToLocalDateTime.apply(heartRateVo.getStartTime()));
                endTimeCol.append(DateUtils.convertTimestampToLocalDateTime.apply(heartRateVo.getEndTime()));
                heartRateCol.append(heartRateVo.getHeartRate());
                heartRateMinCol.append(heartRateVo.getHeartRateMin());
                heartRateMaxCol.append(heartRateVo.getHeartRateMax());
            }
            table.addColumns(startTimeCol, endTimeCol, heartRateCol, heartRateMinCol, heartRateMaxCol);
        }
        return table;
    };

    public static final Function<List<CaloriesBurnedVo>, Table> caloriesBurnedVoListTableFunction = caloriesBurnedVoList -> {
        // Create the table and define the column structure first.
        final Table table = Table.create("Calories Burned Data");

        // Check if the list is valid before proceeding.
        if (Validator.isNotEmptyList(caloriesBurnedVoList)) {
            // 1. Create empty columns that will hold the data.
            final StringColumn uuidCol = StringColumn.create("uuid");
            final IntColumn ageCol = IntColumn.create("age");
            final StringColumn genderCol = StringColumn.create("gender");
            final DoubleColumn heightCol = DoubleColumn.create("height");
            final DoubleColumn weightCol = DoubleColumn.create("weight");
            final IntColumn stepCountCol = IntColumn.create("stepCount");
            final StringColumn activityList = StringColumn.create("activityList");

            // 2. Iterate through the list ONCE, appending data to each column.
            for (final CaloriesBurnedVo vo : caloriesBurnedVoList) {
                uuidCol.append(vo.getUuid());
                ageCol.append(vo.getAge());
                genderCol.append(vo.getGender());
                heightCol.append(vo.getHeight());
                weightCol.append(vo.getWeight());
                stepCountCol.append(vo.getStepCount());
                activityList.append(vo.getActivityList().toString());
            }

            // 3. Add the now-populated columns to the table.
            table.addColumns(uuidCol, ageCol, genderCol, heightCol, weightCol, stepCountCol, activityList);
        }
        return table;
    };
}
