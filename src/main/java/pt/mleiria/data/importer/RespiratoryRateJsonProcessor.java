package pt.mleiria.data.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.core.StopWatch;
import pt.mleiria.data.importer.config.DataLocation;
import pt.mleiria.db.JsonDocument;
import pt.mleiria.vo.RespiratoryRateVo;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class RespiratoryRateJsonProcessor implements GenericJsonProcessor {

    private static final Logger logger = LoggerFactory.getLogger(RespiratoryRateJsonProcessor.class);

    private final Function<Path, Stream<JsonDocument>> respRatePathToJsonDocumentsBiFunction = this::respRatePathToJsonDocuments;

    private Stream<JsonDocument> respRatePathToJsonDocuments(Path path) {
        return pathToJsonDocumentsGeneric(
                path,
                new TypeReference<List<RespiratoryRateVo>>() {
                });
    }

    @Override
    public void processJsonFilesInFolder(final DataSource ds) {
        final DataLocation dataLocation = DataLocation.RESPIRATORY_RATE;
        final Path startPath = getStartPath(dataLocation);
        final StopWatch sw = new StopWatch();
        logger.info("Starting {} for JSON files...", this.getClass().getName());
        processDirectory(startPath, ds, respRatePathToJsonDocumentsBiFunction, dataLocation);
        logger.info("Running time: {} secs", sw.stop());
    }
}
