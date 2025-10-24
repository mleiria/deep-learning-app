package pt.mleiria.core;

import org.junit.jupiter.api.Test;
import tech.tablesaw.api.Table;

import static org.junit.jupiter.api.Assertions.*;

class CaloriesBurnedQueryExecTest {

    @Test
    void execQueryForJson() {
        final String sql = "select data from calories_burned_details where (data ->> 'activityList') IS NOT NULL";
        final QueryExec  queryExec = new CaloriesBurnedQueryExec();
        final Table table = queryExec.execQueryForJson(sql);
        System.out.println(table);
        assertNotNull(table);
    }
}