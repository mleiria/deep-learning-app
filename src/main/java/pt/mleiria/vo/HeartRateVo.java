package pt.mleiria.vo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for com.samsung.shealth.tracker.heart_rate
 * Represents an aggregated heart rate summary over a specific time interval.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeartRateVo {

    @JsonProperty("start_time")
    private long startTime;

    @JsonProperty("end_time")
    private long endTime;

    @JsonProperty("heart_rate")
    private float heartRate;

    @JsonProperty("heart_rate_max")
    private float heartRateMax;

    @JsonProperty("heart_rate_min")
    private float heartRateMin;

    // Default constructor
    public HeartRateVo() {
    }

    // --- Getters and Setters ---

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public float getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(float heartRate) {
        this.heartRate = heartRate;
    }

    public float getHeartRateMax() {
        return heartRateMax;
    }

    public void setHeartRateMax(float heartRateMax) {
        this.heartRateMax = heartRateMax;
    }

    public float getHeartRateMin() {
        return heartRateMin;
    }

    public void setHeartRateMin(float heartRateMin) {
        this.heartRateMin = heartRateMin;
    }

    @Override
    public String toString() {
        return "HeartRateTrackerVo{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", heartRate=" + heartRate +
                ", heartRateMax=" + heartRateMax +
                ", heartRateMin=" + heartRateMin +
                '}';
    }
}
