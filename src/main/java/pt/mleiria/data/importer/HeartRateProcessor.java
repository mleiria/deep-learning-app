package pt.mleiria.data.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.core.StopWatch;
import pt.mleiria.data.importer.config.Config;
import pt.mleiria.data.importer.config.DataLocation;
import pt.mleiria.db.JsonDocument;
import pt.mleiria.vo.AdvancedGlycationEndproductRawVo;
import pt.mleiria.vo.HeartRateVo;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class HeartRateProcessor implements GenericProcessor{

    private static final Logger logger = LoggerFactory.getLogger(HeartRateProcessor.class);


    private final Function<Path, Stream<JsonDocument>> heartRatePathToJsonDocumentsFunction = this::heartRatePathToJsonDocuments;

    private Stream<JsonDocument> heartRatePathToJsonDocuments(Path path) {
        return pathToJsonDocumentsGeneric(
                path,
                new TypeReference<List<HeartRateVo>>() {
                });
    }

    @Override
    public void processJsonFilesInFolder(final DataSource ds) {
        final DataLocation dataLocation = DataLocation.HEART_RATE;
        final Path startPath = getStartPath(dataLocation);
        final StopWatch sw = new StopWatch();
        logger.info("Starting {} for JSON files...", this.getClass().getName());
        processDirectory(startPath, ds, heartRatePathToJsonDocumentsFunction, dataLocation);
        logger.info("Running time: {} secs", sw.stop());
    }
}
