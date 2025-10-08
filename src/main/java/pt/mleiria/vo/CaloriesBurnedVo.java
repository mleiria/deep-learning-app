package pt.mleiria.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A Value Object (VO) representing the detailed summary of calories burned,
 * mapping to the com.samsung.shealth.calories_burned.details data structure.
 */
public class CaloriesBurnedVo {

    @JsonProperty("activityLevel")
    private int activityLevel;

    @JsonProperty("activityList")
    private List<ActivityDetail> activityList;

    @JsonProperty("age")
    private int age;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("height")
    private double height;

    @JsonProperty("stepCount")
    private int stepCount;

    @JsonProperty("weight")
    private double weight;

    // Default no-argument constructor
    public CaloriesBurnedVo() {
    }

    // --- Getters and Setters ---

    public int getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }

    public List<ActivityDetail> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<ActivityDetail> activityList) {
        this.activityList = activityList;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "CaloriesBurnedVo{" +
                "activityLevel=" + activityLevel +
                ", activityList=" + activityList +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", height=" + height +
                ", stepCount=" + stepCount +
                ", weight=" + weight +
                '}';
    }

    /**
     * A nested static class to represent individual items in the activityList.
     */
    public static class ActivityDetail {

        @JsonProperty("activeTime")
        private long activeTime;

        @JsonProperty("calorie")
        private double calorie;

        @JsonProperty("distance")
        private double distance;

        @JsonProperty("type")
        private int type;

        // Default no-argument constructor
        public ActivityDetail() {
        }

        // --- Getters and Setters for ActivityDetail ---

        public long getActiveTime() {
            return activeTime;
        }

        public void setActiveTime(long activeTime) {
            this.activeTime = activeTime;
        }

        public double getCalorie() {
            return calorie;
        }

        public void setCalorie(double calorie) {
            this.calorie = calorie;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "ActivityDetail{" +
                    "activeTime=" + activeTime +
                    ", calorie=" + calorie +
                    ", distance=" + distance +
                    ", type=" + type +
                    '}';
        }
    }
}