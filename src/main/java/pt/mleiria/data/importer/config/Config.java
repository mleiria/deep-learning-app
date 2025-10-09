package pt.mleiria.data.importer.config;

public enum Config {

    DB_URL_REMOTE("jdbc:postgresql://manuel-hs:5432/workout_tracker"),
    DB_URL_LOCAL("jdbc:postgresql://localhost:5432/workout_tracker"),
    USER("myuser"),
    PASSWORD("mysecretpassword"),
    DATA_FOLDER("/home/manuel/Downloads/Databases/samsunghealth_manuel.leiria_20250930125884/jsons/");

    public final String value;
    Config(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
