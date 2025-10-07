package pt.mleiria.vo;

public enum DataLocation {

    ADVANCED_GLYCATION_ENDPRODUCT("com.samsung.health.advanced_glycation_endproduct.raw", "advanced_glycation_endproduct_raw"),
    HRV("com.samsung.health.hrv", "hrv"),
    MOVEMENT("com.samsung.health.movement", "movement"),
    RESPIRATORY_RATE("com.samsung.health.respiratory_rate", "respiratory_rate");


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
