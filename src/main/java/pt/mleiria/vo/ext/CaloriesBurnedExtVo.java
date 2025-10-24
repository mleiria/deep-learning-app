package pt.mleiria.vo.ext;

import com.opencsv.bean.CsvBindByName;

public class CaloriesBurnedExtVo {

    @CsvBindByName(column = "active_calories_goal")
    private Double activeCaloriesGoal;

    @CsvBindByName(column = "version")
    private Integer version;

    @CsvBindByName(column = "extra_data")
    private String extraData;

    @CsvBindByName(column = "exercise_calories")
    private Integer exerciseCalories;

    @CsvBindByName(column = "total_exercise_calories")
    private Integer totalExerciseCalories;

    @CsvBindByName(column = "com.samsung.shealth.calories_burned.create_sh_ver")
    private Integer createShealthVer;

    @CsvBindByName(column = "com.samsung.shealth.calories_burned.tef_calorie")
    private Double tefCalorie;

    @CsvBindByName(column = "com.samsung.shealth.calories_burned.active_time")
    private Long activeTime;

    @CsvBindByName(column = "com.samsung.shealth.calories_burned.rest_calorie")
    private Double restCalorie;

    @CsvBindByName(column = "com.samsung.shealth.calories_burned.modify_sh_ver")
    private Integer modifyShealthVer;

    @CsvBindByName(column = "com.samsung.shealth.calories_burned.update_time")
    private String updateTime; // Can be parsed to LocalDateTime if needed

    @CsvBindByName(column = "com.samsung.shealth.calories_burned.create_time")
    private String createTime; // Can be parsed to LocalDateTime if needed

    @CsvBindByName(column = "com.samsung.shealth.calories_burned.active_calorie")
    private Double activeCalorie;

    @CsvBindByName(column = "com.samsung.shealth.calories_burned.deviceuuid")
    private String deviceUuid;

    @CsvBindByName(column = "com.samsung.shealth.calories_burned.pkg_name")
    private String packageName;

    @CsvBindByName(column = "com.samsung.shealth.calories_burned.datauuid")
    private String dataUuid;

    @CsvBindByName(column = "com.samsung.shealth.calories_burned.day_time")
    private Long dayTime; // Unix timestamp in milliseconds

    // Default constructor
    public CaloriesBurnedExtVo() {
    }

    // Getters and Setters for all fields

    public Double getActiveCaloriesGoal() {
        return activeCaloriesGoal;
    }

    public void setActiveCaloriesGoal(Double activeCaloriesGoal) {
        this.activeCaloriesGoal = activeCaloriesGoal;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public Integer getExerciseCalories() {
        return exerciseCalories;
    }

    public void setExerciseCalories(Integer exerciseCalories) {
        this.exerciseCalories = exerciseCalories;
    }

    public Integer getTotalExerciseCalories() {
        return totalExerciseCalories;
    }

    public void setTotalExerciseCalories(Integer totalExerciseCalories) {
        this.totalExerciseCalories = totalExerciseCalories;
    }

    public Integer getCreateShealthVer() {
        return createShealthVer;
    }

    public void setCreateShealthVer(Integer createShealthVer) {
        this.createShealthVer = createShealthVer;
    }

    public Double getTefCalorie() {
        return tefCalorie;
    }

    public void setTefCalorie(Double tefCalorie) {
        this.tefCalorie = tefCalorie;
    }

    public Long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Long activeTime) {
        this.activeTime = activeTime;
    }

    public Double getRestCalorie() {
        return restCalorie;
    }

    public void setRestCalorie(Double restCalorie) {
        this.restCalorie = restCalorie;
    }

    public Integer getModifyShealthVer() {
        return modifyShealthVer;
    }

    public void setModifyShealthVer(Integer modifyShealthVer) {
        this.modifyShealthVer = modifyShealthVer;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Double getActiveCalorie() {
        return activeCalorie;
    }

    public void setActiveCalorie(Double activeCalorie) {
        this.activeCalorie = activeCalorie;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDataUuid() {
        return dataUuid;
    }

    public void setDataUuid(String dataUuid) {
        this.dataUuid = dataUuid;
    }

    public Long getDayTime() {
        return dayTime;
    }

    public void setDayTime(Long dayTime) {
        this.dayTime = dayTime;
    }

    @Override
    public String toString() {
        return "CaloriesBurned{" +
                "activeCaloriesGoal=" + activeCaloriesGoal +
                ", version=" + version +
                ", extraData='" + extraData + '\'' +
                ", exerciseCalories=" + exerciseCalories +
                ", totalExerciseCalories=" + totalExerciseCalories +
                ", createShealthVer=" + createShealthVer +
                ", tefCalorie=" + tefCalorie +
                ", activeTime=" + activeTime +
                ", restCalorie=" + restCalorie +
                ", modifyShealthVer=" + modifyShealthVer +
                ", updateTime='" + updateTime + '\'' +
                ", createTime='" + createTime + '\'' +
                ", activeCalorie=" + activeCalorie +
                ", deviceUuid='" + deviceUuid + '\'' +
                ", packageName='" + packageName + '\'' +
                ", dataUuid='" + dataUuid + '\'' +
                ", dayTime=" + dayTime +
                '}';
    }
}