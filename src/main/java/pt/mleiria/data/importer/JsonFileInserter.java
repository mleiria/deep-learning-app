package pt.mleiria.data.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.Config;
import pt.mleiria.DataSourceFactory;
import pt.mleiria.core.JacksonUtils;
import pt.mleiria.core.StopWatch;
import pt.mleiria.vo.AdvancedGlycationEndproductRawVo;
import pt.mleiria.vo.DataLocation;
import pt.mleiria.vo.HrvVo;
import pt.mleiria.vo.JsonDocument;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class JsonFileInserter {


    private static final Logger logger = LoggerFactory.getLogger(JsonFileInserter.class);



    public static void main(String[] args) {
        logger.info("Starting JsonFileInserter");
        final DataSource ds = DataSourceFactory.getDataSource();

        final AgeProcessor ageProcessor = new AgeProcessor();
        ageProcessor.processJsonFilesInFolder(ds);

        final HrvProcessor hrvProcessor = new HrvProcessor();
        hrvProcessor.processJsonFilesInFolder(ds);

        final MovementProcessor movementProcessor = new MovementProcessor();
        movementProcessor.processJsonFilesInFolder(ds);

        final RespiratoryRateProcessor respiratoryRateProcessor = new RespiratoryRateProcessor();
        respiratoryRateProcessor.processJsonFilesInFolder(ds);
    }
















}