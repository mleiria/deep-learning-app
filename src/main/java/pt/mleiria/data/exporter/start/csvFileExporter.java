package pt.mleiria.data.exporter.start;

import pt.mleiria.config.ConfigLoader;
import pt.mleiria.data.GenericCsvProcessor;
import pt.mleiria.data.exporter.HearthRateDataCsvProcessor;

public class csvFileExporter {

    public static void main(String[] args) {
        final GenericCsvProcessor genericCsvProcessor = new HearthRateDataCsvProcessor();
        genericCsvProcessor.writeDataToCsv(ConfigLoader.INSTANCE.getDataFolderCsv() + "heartRateRaw.csv");
    }
}
