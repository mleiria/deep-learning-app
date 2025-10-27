package pt.mleiria.core;

import org.junit.jupiter.api.Test;
import tech.tablesaw.api.Table;

import static org.junit.jupiter.api.Assertions.*;

class CaloriesBurnedQueryExecTest {

    @Test
    void execQueryForJson() {
        final CaloriesBurnedQueryExec  queryExec = new CaloriesBurnedQueryExec();
        final Table table = queryExec.execQueryForJson();
        System.out.println(table);
        assertNotNull(table);
    }
}