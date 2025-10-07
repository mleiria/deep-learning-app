package pt.mleiria.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A POJO representing Heart Rate Variability (HRV) data from Samsung Health.
 * com.samsung.health.hrv: This is the identifier for Heart Rate Variability data within the Samsung Health app.[1]
 * <p>
 * "start_time": 1757286028038: This is the start time of the HRV measurement. It's represented in Unix timestamp format
 * in milliseconds. This timestamp corresponds to Wednesday, November 5, 2025, at 12:20:28.038 PM GMT.
 * <p>
 * "end_time": 1757286327631: This is the end time of the HRV measurement, also in Unix timestamp format in milliseconds.
 * This timestamp corresponds to Wednesday, November 5, 2025, at 12:25:27.631 PM GMT. The duration of this particular measurement was approximately 5 minutes.
 * <p>
 * "sdnn": 58.19912: SDNN stands for the "Standard Deviation of NN intervals."[2] NN intervals refer to the time between consecutive "normal" heartbeats.
 * What it indicates: SDNN gives an indication of your overall HRV.[2] A higher SDNN value is generally associated with better cardiovascular health
 * and a greater ability to handle stress.[3] For a 24-hour recording, SDNN values above 100 ms are considered healthy,
 * values between 50-100 ms suggest compromised health, and values below 50 ms are classified as unhealthy.[4]
 * However, it's important to note that these are general guidelines for long-term measurements, and shorter-term readings can vary.[2]
 * <p>
 * "rmssd": 56.467903: RMSSD stands for the "Root Mean Square of Successive Differences" between normal heartbeats.[3]
 * What it indicates: This metric is a primary measure of your parasympathetic nervous system's influence on your heart rate,
 * which is responsible for the "rest-and-digest" response.[2][5] RMSSD is particularly useful for tracking short-term changes in
 * HRV related to factors like daily stress, training recovery, and sleep quality.[6] A higher RMSSD value generally indicates
 * better recovery and a more relaxed state.[7] Normal RMSSD values for healthy adults can range from 27 to 72 ms, but this
 * can be influenced by age and fitness level.[2]
 * <p>
 * In essence, this data provides a glimpse into your autonomic nervous system's function during a specific 5-minute window.
 * By tracking these HRV metrics over time, you can gain insights into your body's stress levels, recovery status, and overall cardiovascular health.
 * It is important to look for trends in your personal data rather than focusing on a single reading.[8]
 */
public class HrvVo {

    /**
     * The start time of the HRV measurement in Unix milliseconds.
     */
    @JsonProperty("start_time")
    private long startTime;

    /**
     * The end time of the HRV measurement in Unix milliseconds.
     */
    @JsonProperty("end_time")
    private long endTime;

    /**
     * SDNN (Standard Deviation of NN intervals) in milliseconds.
     * This reflects overall heart rate variability.
     */
    @JsonProperty("sdnn")
    private double sdnn;

    /**
     * RMSSD (Root Mean Square of Successive Differences) in milliseconds.
     * This is a key indicator of parasympathetic nervous system activity.
     */
    @JsonProperty("rmssd")
    private double rmssd;

    // Default no-argument constructor
    public HrvVo() {
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

    public double getSdnn() {
        return sdnn;
    }

    public void setSdnn(double sdnn) {
        this.sdnn = sdnn;
    }

    public double getRmssd() {
        return rmssd;
    }

    public void setRmssd(double rmssd) {
        this.rmssd = rmssd;
    }

    /**
     * Provides a string representation of the HrvData object.
     *
     * @return A string detailing the object's fields.
     */
    @Override
    public String toString() {
        return "HrvData{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", sdnn=" + sdnn +
                ", rmssd=" + rmssd +
                '}';
    }
}
