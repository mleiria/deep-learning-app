package pt.mleiria.data.importer.config;

public enum DataLocation {

    ADVANCED_GLYCATION_ENDPRODUCT("com.samsung.health.advanced_glycation_endproduct.raw", "advanced_glycation_endproduct_raw"),
    HRV("com.samsung.health.hrv", "hrv"),
    MOVEMENT("com.samsung.health.movement", "movement"),
    RESPIRATORY_RATE("com.samsung.health.respiratory_rate", "respiratory_rate"),
    CALORIES_BURNED("com.samsung.shealth.calories_burned.details", "calories_burned_details"),
    EXERCISE("com.samsung.shealth.exercise", "exercise"),
    RECOVERY_HEART_RATE("com.samsung.shealth.exercise.recovery_heart_rate", "recovery_heart_rate"),
    HEART_RATE("com.samsung.shealth.tracker.heart_rate", "heart_rate");


    public final String folderName;
    public final String tableName;

    DataLocation(String folderName, String tableName) {
        this.folderName = folderName;
        this.tableName = tableName;
    }

    public String getFolderName() {
        return folderName;
    }
    public String getTableName() {
        return tableName;
    }
}
