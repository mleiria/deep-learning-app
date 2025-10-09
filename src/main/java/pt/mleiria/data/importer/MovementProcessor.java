package pt.mleiria.data.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.data.importer.config.Config;
import pt.mleiria.core.StopWatch;
import pt.mleiria.data.importer.config.DataLocation;
import pt.mleiria.db.JsonDocument;
import pt.mleiria.vo.MovementVo;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class MovementProcessor implements GenericProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MovementProcessor.class);

    private final Function<Path, Stream<JsonDocument>> movementPathToJsonDocumentsFunction = this::movementPathToJsonDocuments;

    private Stream<JsonDocument> movementPathToJsonDocuments(Path path) {
        return pathToJsonDocumentsGeneric(
                path,
                new TypeReference<List<MovementVo>>() {
                });
    }

    @Override
    public void processJsonFilesInFolder(final DataSource ds) {
        final DataLocation dataLocation = DataLocation.MOVEMENT;
        final Path startPath = getStartPath(dataLocation);
        final StopWatch sw = new StopWatch();
        logger.info("Starting {} for JSON files...", this.getClass().getName());
        processDirectory(startPath, ds, movementPathToJsonDocumentsFunction, dataLocation);
        logger.info("Running time: {} secs", sw.stop());
    }
}
