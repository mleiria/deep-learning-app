package pt.mleiria.db;

import java.nio.file.Path;

public record JsonDocument(Path sourcePath, String jsonContent) {
}
