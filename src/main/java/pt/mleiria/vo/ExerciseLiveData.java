package pt.mleiria.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO for ...com.samsung.health.exercise.live_data.json
 * Represents live, time-series data points during an exercise.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExerciseLiveData {

    @JsonProperty("start_time")
    private long startTime;

    @JsonProperty("distance")
    private Double distance;

    @JsonProperty("speed")
    private Double speed;

    @JsonProperty("heart_rate")
    private Float heartRate;

    // Default constructor
    public ExerciseLiveData() {
    }

    // --- Getters and Setters ---

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Float getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Float heartRate) {
        this.heartRate = heartRate;
    }

    @Override
    public String toString() {
        return "ExerciseLiveData{" +
                "startTime=" + startTime +
                ", distance=" + distance +
                ", speed=" + speed +
                ", heartRate=" + heartRate +
                '}';
    }
}
