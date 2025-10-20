package pt.mleiria.data.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.core.StopWatch;
import pt.mleiria.vo.CaloriesBurnedVo;
import pt.mleiria.data.importer.config.DataLocation;
import pt.mleiria.db.JsonDocument;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class CaloriesBurnedJsonProcessor implements GenericJsonProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CaloriesBurnedJsonProcessor.class);

    private final Function<Path, Stream<JsonDocument>> calsBurnedPathToJsonDocumentsFunction = this::calsBurnedPathToJsonDocuments;

    private Stream<JsonDocument> calsBurnedPathToJsonDocuments(Path path) {
        return pathToJsonDocumentsGeneric(
                path,
                new TypeReference<List<CaloriesBurnedVo>>() {
                });
    }

    @Override
    public void processJsonFilesInFolder(final DataSource ds) {
        final DataLocation dataLocation = DataLocation.CALORIES_BURNED;
        final Path startPath = getStartPath(dataLocation);
        final StopWatch sw = new StopWatch();
        logger.info("Starting {} for JSON files...", this.getClass().getName());
        processDirectory(startPath, ds, calsBurnedPathToJsonDocumentsFunction, dataLocation);
        logger.info("Running time: {} secs", sw.stop());
    }
}
