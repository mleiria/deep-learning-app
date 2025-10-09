package pt.mleiria.data.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.core.StopWatch;
import pt.mleiria.data.importer.config.Config;
import pt.mleiria.data.importer.config.DataLocation;
import pt.mleiria.db.JsonDocument;
import pt.mleiria.vo.AdvancedGlycationEndproductRawVo;
import pt.mleiria.vo.RecoveryHeartRateVo;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class RecoveryHeartRateProcessor implements GenericProcessor{

    private static final Logger logger = LoggerFactory.getLogger(RecoveryHeartRateProcessor.class);


    private final Function<Path, Stream<JsonDocument>> recHeartRateProcPathToJsonDocumentsFunction = this::recHeartRateProcPathToJsonDocuments;

    private Stream<JsonDocument> recHeartRateProcPathToJsonDocuments(Path path) {
        return pathToJsonDocumentsGeneric(
                path,
                new TypeReference<List<RecoveryHeartRateVo>>() {
                });
    }

    @Override
    public void processJsonFilesInFolder(final DataSource ds) {
        final DataLocation dataLocation = DataLocation.RECOVERY_HEART_RATE;
        final Path startPath = getStartPath(dataLocation);
        final StopWatch sw = new StopWatch();
        logger.info("Starting {} for JSON files...", this.getClass().getName());
        processDirectory(startPath, ds, recHeartRateProcPathToJsonDocumentsFunction, dataLocation);
        logger.info("Running time: {} secs", sw.stop());
    }
}
