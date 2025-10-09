package pt.mleiria.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * POJO for com.samsung.shealth.exercise.recovery_heart_rate
 * Represents the heart rate data collected during the recovery phase after an exercise.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecoveryHeartRateVo {

    @JsonProperty("is_valid")
    private boolean isValid;

    @JsonProperty("sampling_rate")
    private int samplingRate;

    @JsonProperty("chart_data")
    private List<ChartDataPoint> chartData;

    // Default constructor
    public RecoveryHeartRateVo() {
    }

    // --- Nested Class for chart_data items ---

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChartDataPoint {

        @JsonProperty("start_time")
        private long startTime;

        @JsonProperty("elapsed_time")
        private long elapsedTime;

        @JsonProperty("heart_rate")
        private float heartRate;

        // Getters and Setters
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getElapsedTime() { return elapsedTime; }
        public void setElapsedTime(long elapsedTime) { this.elapsedTime = elapsedTime; }
        public float getHeartRate() { return heartRate; }
        public void setHeartRate(float heartRate) { this.heartRate = heartRate; }

        @Override
        public String toString() {
            return "ChartDataPoint{" +
                    "startTime=" + startTime +
                    ", elapsedTime=" + elapsedTime +
                    ", heartRate=" + heartRate +
                    '}';
        }
    }

    // --- Getters and Setters for the main class ---

    public boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public int getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }

    public List<ChartDataPoint> getChartData() {
        return chartData;
    }

    public void setChartData(List<ChartDataPoint> chartData) {
        this.chartData = chartData;
    }

    @Override
    public String toString() {
        return "RecoveryHeartRateVo{" +
                "isValid=" + isValid +
                ", samplingRate=" + samplingRate +
                ", chartData=" + chartData +
                '}';
    }
}
