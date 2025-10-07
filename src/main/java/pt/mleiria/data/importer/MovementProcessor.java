package pt.mleiria.data.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import pt.mleiria.Config;
import pt.mleiria.core.JacksonUtils;
import pt.mleiria.core.StopWatch;
import pt.mleiria.vo.DataLocation;
import pt.mleiria.vo.JsonDocument;
import pt.mleiria.vo.MovementVo;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MovementProcessor implements GenericProcessor{

    private final Function<MovementVo, String> movementToUniqueString =
            movementVo -> movementVo.getStartTime() + "|" + movementVo.getEndTime();

    private final BiFunction<Path, Set<String>, Stream<JsonDocument>> movementPathToJsonDocumentsBiFunction = this::movementPathToJsonDocuments;

    private Stream<JsonDocument> movementPathToJsonDocuments(Path path, Set<String> existingData) {
        return pathToJsonDocumentsGeneric(
                path,
                existingData,
                new TypeReference<List<MovementVo>>() {
                },
                movementToUniqueString
        );
    }

    private final DataLocation dataLocation = DataLocation.MOVEMENT;

    @Override
    public void processJsonFilesInFolder(final DataSource ds) {
        final Path startPath = Paths.get(Config.DATA_FOLDER.getValue() + dataLocation.getFolderName());
        // Get the existing data to avoid duplicates.
        final StopWatch sw = new StopWatch();
        final Set<String> existingData = listJsonData(ds, dataLocation.getTableName())
                .stream()
                .parallel()
                .map(elem -> JacksonUtils.optionalDecode(elem, MovementVo.class))
                .flatMap(Optional::stream)
                .map(movementToUniqueString)
                .collect(Collectors.toSet());

        logger.info("Running time: {} secs", sw.stop());
        logger.info("Starting {} for JSON files...", this.getClass().getName());
        sw.start();
        processDirectory(startPath, ds, existingData, movementPathToJsonDocumentsBiFunction, dataLocation);
        logger.info("Running time: {} secs", sw.stop());
    }
}
