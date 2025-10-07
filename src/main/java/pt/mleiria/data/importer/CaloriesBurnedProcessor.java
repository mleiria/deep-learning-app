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

public class CaloriesBurnedProcessor implements GenericProcessor{


    @Override
    public void processJsonFilesInFolder(DataSource ds) {

    }
}
