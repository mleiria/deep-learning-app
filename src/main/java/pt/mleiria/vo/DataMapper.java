package pt.mleiria.vo;

import pt.mleiria.core.Validator;
import tech.tablesaw.api.Table;

import java.util.List;
import java.util.function.Function;

public class DataMapper {

    public static final Function<HeartRateVo, Table> heartRateVoTableFunction = heartRateVo -> Table.create("Heart Rate Data")
            .addColumns(
                    tech.tablesaw.api.LongColumn.create("startTime", heartRateVo.getStartTime()),
                    tech.tablesaw.api.LongColumn.create("endTime", heartRateVo.getEndTime()),
                    tech.tablesaw.api.DoubleColumn.create("heartRate", heartRateVo.getHeartRate()),
                    tech.tablesaw.api.DoubleColumn.create("heartRateMin", heartRateVo.getHeartRateMin()),
                    tech.tablesaw.api.DoubleColumn.create("heartRateMax", heartRateVo.getHeartRateMax())
            );

    public static final Function<List<HeartRateVo>, Table> heartRateVoListTableFunction = heartRateVoList -> {
        final Table table = Table.create("Heart Rate Data");
        if (Validator.isNotEmptyList(heartRateVoList)) {
            table.addColumns(
                    tech.tablesaw.api.LongColumn.create("startTime", heartRateVoList.stream().mapToLong(HeartRateVo::getStartTime).toArray()),
                    tech.tablesaw.api.LongColumn.create("endTime", heartRateVoList.stream().mapToLong(HeartRateVo::getEndTime).toArray()),
                    tech.tablesaw.api.DoubleColumn.create("heartRate", heartRateVoList.stream().mapToDouble(HeartRateVo::getHeartRate).toArray()),
                    tech.tablesaw.api.DoubleColumn.create("heartRateMin", heartRateVoList.stream().mapToDouble(HeartRateVo::getHeartRateMin).toArray()),
                    tech.tablesaw.api.DoubleColumn.create("heartRateMax", heartRateVoList.stream().mapToDouble(HeartRateVo::getHeartRateMax).toArray())
            );
        }
        return table;
    };
}
