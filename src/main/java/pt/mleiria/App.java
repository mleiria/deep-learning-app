package pt.mleiria;

import tech.tablesaw.aggregate.AggregateFunctions;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class App {

    public static void main(String[] args) {
        // 1. Create some sample data
        Table sensorReadings = Table.create("Sensor Data",
                DateTimeColumn.create("Timestamp",
                        LocalDateTime.of(2023, 10, 27, 10, 5, 30),
                        LocalDateTime.of(2023, 10, 27, 10, 25, 0),
                        LocalDateTime.of(2023, 10, 27, 10, 55, 15),
                        LocalDateTime.of(2023, 10, 27, 11, 10, 0),
                        LocalDateTime.of(2023, 10, 27, 11, 45, 0),
                        LocalDateTime.of(2023, 10, 27, 12, 1, 0)
                ),
                DoubleColumn.create("Temperature",
                        20.1, 20.5, 20.8, 22.3, 22.9, 23.5
                ),
                IntColumn.create("Humidity",
                        45, 46, 48, 50, 52, 55
                )
        );

        System.out.println("Original Data:");
        System.out.println(sensorReadings);

        // --- The Core Logic ---

        // 2. Create the new "Hour" column by truncating the timestamp
        // It's good practice to work on a copy if you want to keep the original table unmodified.
        Table workingTable = sensorReadings.copy();

        DateTimeColumn timestampCol = workingTable.dateTimeColumn("Timestamp");

        // Use map() to apply the truncation to each element and create a new column
        DateTimeColumn hourGroupCol = timestampCol.map(dt -> dt.truncatedTo(ChronoUnit.HOURS));
        hourGroupCol.setName("Hourly Timestamp");

        // Add the new grouping column to our working table
        workingTable.addColumns(hourGroupCol);

        System.out.println("\nData with Grouping Column:");
        System.out.println(workingTable);


        /// 3. Perform the aggregation using the older summarize-and-join pattern

// Step 3a: Summarize Temperature and count the readings
        Table tempSummary = workingTable.summarize("Temperature", AggregateFunctions.mean)
                .by("Hourly Timestamp");
        Table countSummary = workingTable.summarize("Timestamp", AggregateFunctions.count)
                .by("Hourly Timestamp");

        // as long as you declare them in the initial summarize() call
// Step 3b: Summarize Humidity separately
        Table humiditySummary = workingTable.summarize(
                "Humidity", AggregateFunctions.mean
        ).by("Hourly Timestamp");

// Step 3c: Join the two summary tables together on the grouping column
        Table hourlySummary0 = tempSummary.joinOn("Hourly Timestamp").inner(humiditySummary);
        Table hourlySummary = hourlySummary0.joinOn("Hourly Timestamp").inner(countSummary);


// Rename the summary columns for clarity
        hourlySummary.column("Mean [Temperature]").setName("Avg Temperature");
        hourlySummary.column("Mean [Humidity]").setName("Avg Humidity");
        hourlySummary.column("Count [Timestamp]").setName("Reading Count");

        System.out.println("\nFinal Hourly Summary:");
        System.out.println(hourlySummary);



        System.out.println("\nFinal Hourly Summary:");
        System.out.println(hourlySummary);
    }


}
