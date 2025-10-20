package pt.mleiria.data.exporter.start;

import pt.mleiria.data.exporter.GenericCsvProcessor;
import pt.mleiria.data.exporter.HearthRateDataCsvProcessor;
import pt.mleiria.data.importer.config.Config;

public class csvFileExporter {

    public static void main(String[] args) {
        final GenericCsvProcessor genericCsvProcessor = new HearthRateDataCsvProcessor();
        genericCsvProcessor.writeDataToCsv(Config.DATA_FOLDER_CSV.value + "heartRate.csv");
    }
}
