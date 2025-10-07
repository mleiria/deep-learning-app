package pt.mleiria.vo;

import java.nio.file.Path;

public record JsonDocument(Path sourcePath, String jsonContent) {
}
