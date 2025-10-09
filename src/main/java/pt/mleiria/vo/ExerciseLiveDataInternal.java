package pt.mleiria.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO for ...live_data_internal.json
 * Provides internal metadata for live data points, such as segments.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExerciseLiveDataInternal {

    @JsonProperty("start_time")
    private long startTime;

    @JsonProperty("elapsed_time")
    private long elapsedTime;

    @JsonProperty("segment")
    private int segment;

    @JsonProperty("interval")
    private int interval;

    // Default constructor
    public ExerciseLiveDataInternal() {
    }

    // --- Getters and Setters ---

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public int getSegment() {
        return segment;
    }

    public void setSegment(int segment) {
        this.segment = segment;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        return "ExerciseLiveDataInternal{" +
                "startTime=" + startTime +
                ", elapsedTime=" + elapsedTime +
                ", segment=" + segment +
                ", interval=" + interval +
                '}';
    }
}
