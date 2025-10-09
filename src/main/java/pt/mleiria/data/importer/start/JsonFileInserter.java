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

        final AgeProcessor ageProcessor = new AgeProcessor();
        ageProcessor.processJsonFilesInFolder(ds);

        final HrvProcessor hrvProcessor = new HrvProcessor();
        hrvProcessor.processJsonFilesInFolder(ds);

        final MovementProcessor movementProcessor = new MovementProcessor();
        movementProcessor.processJsonFilesInFolder(ds);

        final RespiratoryRateProcessor respiratoryRateProcessor = new RespiratoryRateProcessor();
        respiratoryRateProcessor.processJsonFilesInFolder(ds);

        final CaloriesBurnedProcessor caloriesBurnedProcessor = new CaloriesBurnedProcessor();
        caloriesBurnedProcessor.processJsonFilesInFolder(ds);

        final ExerciseProcessor exerciseProcessor = new ExerciseProcessor();
        exerciseProcessor.processJsonFilesInFolder(ds);

        final RecoveryHeartRateProcessor recoveryHeartRateProcessor = new RecoveryHeartRateProcessor();
        recoveryHeartRateProcessor.processJsonFilesInFolder(ds);

        final HeartRateProcessor heartRateProcessor = new HeartRateProcessor();
        heartRateProcessor.processJsonFilesInFolder(ds);

        logger.info("Finished JsonFileInserter in {} secs", sw.stop());
    }
















}