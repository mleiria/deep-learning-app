package pt.mleiria.data.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import pt.mleiria.Config;
import pt.mleiria.core.JacksonUtils;
import pt.mleiria.core.StopWatch;
import pt.mleiria.vo.DataLocation;
import pt.mleiria.vo.JsonDocument;
import pt.mleiria.vo.RespiratoryRateVo;

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

public class RespiratoryRateProcessor implements GenericProcessor{

    private final Function<RespiratoryRateVo, String> respRateToUniqueString =
            respRate -> respRate.getStartTime() + "|" + respRate.getEndTime();

    private final BiFunction<Path, Set<String>, Stream<JsonDocument>> respRatePathToJsonDocumentsBiFunction = this::respRatePathToJsonDocuments;

    private Stream<JsonDocument> respRatePathToJsonDocuments(Path path, Set<String> existingData) {
        return pathToJsonDocumentsGeneric(
                path,
                existingData,
                new TypeReference<List<RespiratoryRateVo>>() {
                },
                respRateToUniqueString
        );
    }

    private final DataLocation dataLocation = DataLocation.RESPIRATORY_RATE;

    @Override
    public void processJsonFilesInFolder(final DataSource ds) {
        final Path startPath = Paths.get(Config.DATA_FOLDER.getValue() + dataLocation.getFolderName());
        // Get the existing data to avoid duplicates.
        final StopWatch sw = new StopWatch();
        final Set<String> existingData = listJsonData(ds, dataLocation.getTableName())
                .stream()
                .parallel()
                .map(elem -> JacksonUtils.optionalDecode(elem, RespiratoryRateVo.class))
                .flatMap(Optional::stream)
                .map(respRateToUniqueString)
                .collect(Collectors.toSet());

        logger.info("Running time: {} secs", sw.stop());
        logger.info("Starting {} for JSON files...", this.getClass().getName());
        sw.start();
        processDirectory(startPath, ds, existingData, respRatePathToJsonDocumentsBiFunction, dataLocation);
        logger.info("Running time: {} secs", sw.stop());
    }
}
