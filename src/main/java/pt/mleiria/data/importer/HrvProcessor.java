package pt.mleiria.data.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.Config;
import pt.mleiria.core.StopWatch;
import pt.mleiria.vo.DataLocation;
import pt.mleiria.vo.HrvVo;
import pt.mleiria.vo.JsonDocument;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class HrvProcessor implements GenericProcessor {

    private static final Logger logger = LoggerFactory.getLogger(HrvProcessor.class);

    private final Function<Path, Stream<JsonDocument>> hrvPathToJsonDocumentsBiFunction = this::hrvPathToJsonDocuments;

    private final DataLocation dataLocation = DataLocation.HRV;

    private Stream<JsonDocument> hrvPathToJsonDocuments(Path path) {
        return pathToJsonDocumentsGeneric(
                path,
                new TypeReference<List<HrvVo>>() {
                });
    }


    @Override
    public void processJsonFilesInFolder(final DataSource ds) {
        final Path startPath = Paths.get(Config.DATA_FOLDER.getValue() + dataLocation.getFolderName());
        // Get the existing data to avoid duplicates.
        final StopWatch sw = new StopWatch();

        logger.info("Running time: {} secs", sw.stop());
        logger.info("Starting {} for JSON files...", this.getClass().getName());
        sw.start();
        processDirectory(startPath, ds, hrvPathToJsonDocumentsBiFunction, dataLocation);
        logger.info("Running time: {} secs", sw.stop());
    }


}
