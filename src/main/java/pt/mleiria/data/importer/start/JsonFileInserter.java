package pt.mleiria.data.importer.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.db.DataSourceFactory;
import pt.mleiria.core.StopWatch;
import pt.mleiria.data.importer.*;

import javax.sql.DataSource;


public class JsonFileInserter {


    private static final Logger logger = LoggerFactory.getLogger(JsonFileInserter.class);



    public static void main(String[] args) {
        logger.info("Starting JsonFileInserter");
        final StopWatch sw = new StopWatch();

        final DataSource ds = DataSourceFactory.getDataSource();

        final GenericJsonProcessor ageProcessor = new AgeJsonProcessor();
        ageProcessor.processJsonFilesInFolder(ds);

        final GenericJsonProcessor hrvProcessor = new HrvJsonProcessor();
        hrvProcessor.processJsonFilesInFolder(ds);

        final GenericJsonProcessor movementProcessor = new MovementJsonProcessor();
        movementProcessor.processJsonFilesInFolder(ds);

        final GenericJsonProcessor respiratoryRateProcessor = new RespiratoryRateJsonProcessor();
        respiratoryRateProcessor.processJsonFilesInFolder(ds);

        final GenericJsonProcessor caloriesBurnedProcessor = new CaloriesBurnedJsonProcessor();
        caloriesBurnedProcessor.processJsonFilesInFolder(ds);

        final GenericJsonProcessor exerciseProcessor = new ExerciseJsonProcessor();
        exerciseProcessor.processJsonFilesInFolder(ds);

        final GenericJsonProcessor recoveryHeartRateProcessor = new RecoveryHeartRateJsonProcessor();
        recoveryHeartRateProcessor.processJsonFilesInFolder(ds);

        final GenericJsonProcessor heartRateProcessor = new HeartRateJsonProcessor();
        heartRateProcessor.processJsonFilesInFolder(ds);

        logger.info("Finished JsonFileInserter in {} secs", sw.stop());
    }
















}