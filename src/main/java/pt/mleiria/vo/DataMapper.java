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
            table.addColumns(
                    DateTimeColumn.create(
                            "startTime",
                            heartRateVoList.stream()
                                    .map(vo -> DateUtils.convertTimestampToLocalDateTime.apply(vo.getStartTime()))),
                    DateTimeColumn.create(
                            "endTime",
                            heartRateVoList.stream()
                                    .map(vo -> DateUtils.convertTimestampToLocalDateTime.apply(vo.getEndTime()))),
                            DoubleColumn.create("heartRate", heartRateVoList.stream().mapToDouble(HeartRateVo::getHeartRate).toArray()),
                            DoubleColumn.create("heartRateMin", heartRateVoList.stream().mapToDouble(HeartRateVo::getHeartRateMin).toArray()),
                            DoubleColumn.create("heartRateMax", heartRateVoList.stream().mapToDouble(HeartRateVo::getHeartRateMax).toArray())
                    );
        }
        return table;
    };

    public static final Function<List<CaloriesBurnedVo>, Table> caloriesBurnedVoListTableFunction = caloriesBurnedVoList -> {
        // Create the table and define the column structure first.
        Table table = Table.create("Calories Burned Data");

        // Check if the list is valid before proceeding.
        if (Validator.isNotEmptyList(caloriesBurnedVoList)) {
            // 1. Create empty columns that will hold the data.
            IntColumn ageCol = IntColumn.create("age");
            StringColumn genderCol = StringColumn.create("gender");
            DoubleColumn heightCol = DoubleColumn.create("height");
            DoubleColumn weightCol = DoubleColumn.create("weight");
            IntColumn stepCountCol = IntColumn.create("stepCount");
            StringColumn activityList = StringColumn.create("activityList");

            // 2. Iterate through the list ONCE, appending data to each column.
            for (CaloriesBurnedVo vo : caloriesBurnedVoList) {
                ageCol.append(vo.getAge());
                genderCol.append(vo.getGender());
                heightCol.append(vo.getHeight());
                weightCol.append(vo.getWeight());
                stepCountCol.append(vo.getStepCount());
                activityList.append(vo.getActivityList().toString());
            }

            // 3. Add the now-populated columns to the table.
            table.addColumns(ageCol, genderCol, heightCol, weightCol, stepCountCol, activityList);
        }
        return table;
    };
}
