package pt.mleiria.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.mleiria.IOUtils;
import pt.mleiria.config.ConfigLoader;
import tech.tablesaw.api.Table;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class HeartRateQueryExecTest {


    @BeforeAll
    static void setup() {
    }

    @Test
    void selectAll() {
        final Table heartRate = new HeartRateQueryExec().selectAll();
        assertNotNull(heartRate);
        //assertFalse(heartRate.isEmpty());
        System.out.println("Total rows: " + heartRate.rowCount());
        System.out.println(heartRate.print());
    }

    @Test
    void execQuery() {
        final String query = "SELECT MAX((data ->> 'heart_rate_max')::numeric) AS max_overall_heart_rate FROM heart_rate;";
        final Table heartRate = new HeartRateQueryExec().execQuery(query);
        assertNotNull(heartRate);
        //assertEquals(173.0, heartRate.row(0).getDouble(0));
    }

    @Test
    void execQueryForJson() {
        final String query = "SELECT data FROM heart_rate WHERE (data ->> 'heart_rate_max')::numeric = (SELECT MAX((data ->> 'heart_rate_max')::numeric) FROM heart_rate);";
        final Table heartRate = new HeartRateQueryExec().execQueryForJson(query);
        System.out.println(heartRate);
        assertNotNull(heartRate);
        //assertEquals(173.0, heartRate.row(0).getDouble(0));
    }

    @Test
    void execQueryWithAggregation(){
        final Table heartRate = new HeartRateQueryExec().execQueryWithAggregation(ChronoUnit.DAYS);
        System.out.println(heartRate);
        //System.out.println(heartRate.column("startTime").print());
        System.out.println(heartRate.column("Mean [heartRate]").print());
        assertNotNull(heartRate);
        //IOUtils.save(heartRate, ConfigLoader.INSTANCE.getDataFolderCsv() + "heartRateMean.csv");
    }

    @Test
    void filterByDay(){
        final HeartRateQueryExec heartRateQueryExec = new HeartRateQueryExec();
        final Table filteredTable = heartRateQueryExec.execQueryFilterByDate(2025, 6,9);
        System.out.println(filteredTable.print());
        assertNotNull(filteredTable);
        final Table aggregatedTable = heartRateQueryExec.aggregate(filteredTable, ChronoUnit.HOURS);
        System.out.println(aggregatedTable.print());
        assertNotNull(aggregatedTable);
        IOUtils.save(aggregatedTable, ConfigLoader.INSTANCE.getDataFolderCsv() + "heartRateMean_2026_06_09.csv");
    }
}