package pt.mleiria.data.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.data.importer.config.Config;
import pt.mleiria.core.StopWatch;
import pt.mleiria.data.importer.config.DataLocation;
import pt.mleiria.vo.HrvVo;
import pt.mleiria.db.JsonDocument;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class HrvProcessor implements GenericProcessor {

    private static final Logger logger = LoggerFactory.getLogger(HrvProcessor.class);

    private final Function<Path, Stream<JsonDocument>> hrvPathToJsonDocumentsFunction = this::hrvPathToJsonDocuments;

    private Stream<JsonDocument> hrvPathToJsonDocuments(Path path) {
        return pathToJsonDocumentsGeneric(
                path,
                new TypeReference<List<HrvVo>>() {
                });
    }


    @Override
    public void processJsonFilesInFolder(final DataSource ds) {
        final DataLocation dataLocation = DataLocation.HRV;
        final Path startPath = getStartPath(dataLocation);
        final StopWatch sw = new StopWatch();
        logger.info("Starting {} for JSON files...", this.getClass().getName());
        processDirectory(startPath, ds, hrvPathToJsonDocumentsFunction, dataLocation);
        logger.info("Running time: {} secs", sw.stop());
    }


}
