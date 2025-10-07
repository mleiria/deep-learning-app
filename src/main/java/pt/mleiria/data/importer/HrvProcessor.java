package pt.mleiria.data.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.mleiria.Config;
import pt.mleiria.core.JacksonUtils;
import pt.mleiria.core.StopWatch;
import pt.mleiria.vo.DataLocation;
import pt.mleiria.vo.HrvVo;
import pt.mleiria.vo.JsonDocument;

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

public class HrvProcessor implements GenericProcessor {

    private final Function<HrvVo, String> hrvToUniqueString = hrvVo -> hrvVo.getStartTime() + "|" + hrvVo.getEndTime();

    private final BiFunction<Path, Set<String>, Stream<JsonDocument>> hrvPathToJsonDocumentsBiFunction = this::hrvPathToJsonDocuments;

    private final DataLocation dataLocation = DataLocation.HRV;

    private Stream<JsonDocument> hrvPathToJsonDocuments(Path path, Set<String> existingData) {
        return pathToJsonDocumentsGeneric(
                path,
                existingData,
                new TypeReference<List<HrvVo>>() {
                }, // The specific TypeReference for HrvVo
                hrvToUniqueString                   // The specific function for HrvVo
        );
    }


    @Override
    public void processJsonFilesInFolder(final DataSource ds) {
        final Path startPath = Paths.get(Config.DATA_FOLDER.getValue() + dataLocation.getFolderName());
        // Get the existing data to avoid duplicates.
        final StopWatch sw = new StopWatch();
        final Set<String> existingData = listJsonData(ds, dataLocation.getTableName())
                .stream()
                .parallel()
                .map(elem -> JacksonUtils.optionalDecode(elem, HrvVo.class))
                .flatMap(Optional::stream)
                .map(hrvToUniqueString)
                .collect(Collectors.toSet());

        logger.info("Running time: {} secs", sw.stop());
        logger.info("Starting {} for JSON files...", this.getClass().getName());
        sw.start();
        processDirectory(startPath, ds, existingData, hrvPathToJsonDocumentsBiFunction, dataLocation);
        logger.info("Running time: {} secs", sw.stop());
    }


}
