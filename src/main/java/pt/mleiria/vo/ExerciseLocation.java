package pt.mleiria.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO for ...com.samsung.health.exercise.location_data.json
 * Represents a single GPS data point during an exercise.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExerciseLocation {

    @JsonProperty("start_time")
    private long startTime;

    @JsonProperty("latitude")
    private double latitude;

    @JsonProperty("longitude")
    private double longitude;

    @JsonProperty("altitude")
    private Double altitude;

    @JsonProperty("accuracy")
    private Double accuracy;

    // Default constructor
    public ExerciseLocation() {
    }

    // --- Getters and Setters ---

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public String toString() {
        return "ExerciseLocation{" +
                "startTime=" + startTime +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", accuracy=" + accuracy +
                '}';
    }
}
