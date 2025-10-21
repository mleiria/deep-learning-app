package pt.mleiria.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
    void executeQuery() {
        final String query = "SELECT MAX((data ->> 'heart_rate_max')::numeric) AS max_overall_heart_rate FROM heart_rate;";
        final Table heartRate = new HeartRateQueryExec().executeQuery(query);
        assertNotNull(heartRate);
        //assertEquals(173.0, heartRate.row(0).getDouble(0));
    }

    @Test
    void executeQueryForJson() {
        final String query = "SELECT data FROM heart_rate WHERE (data ->> 'heart_rate_max')::numeric = (SELECT MAX((data ->> 'heart_rate_max')::numeric) FROM heart_rate);";
        final Table heartRate = new HeartRateQueryExec().executeQueryForJson(query);
        System.out.println(heartRate);
        assertNotNull(heartRate);
        //assertEquals(173.0, heartRate.row(0).getDouble(0));
    }

    @Test
    void execQueryWithAggregation(){
        final Table heartRate = new HeartRateQueryExec().execQueryWithAggregation(ChronoUnit.HOURS);
        System.out.println(heartRate);
        assertNotNull(heartRate);
    }
}