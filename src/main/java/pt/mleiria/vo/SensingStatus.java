package pt.mleiria.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO for ...sensing_status.json
 * Contains configuration and metadata about sensors used during the exercise.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensingStatus {

    @JsonProperty("heart_rate")
    private HeartRate heartRate;

    @JsonProperty("heart_rate_zone")
    private HeartRateZone heartRateZone;

    @JsonProperty("sampling_rate")
    private int samplingRate;

    // Default constructor
    public SensingStatus() {
    }

    // --- Nested Classes for sub-objects ---

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HeartRate {

        @JsonProperty("is_valid")
        private boolean isValid;

        @JsonProperty("type")
        private int type;

        @JsonProperty("max_hr_custom")
        private int maxHrCustom;

        @JsonProperty("max_hr_auto")
        private int maxHrAuto;

        @JsonProperty("at")
        private int at;

        @JsonProperty("ant")
        private int ant;

        // Getters and Setters...
        public boolean getIsValid() { return isValid; }
        public void setIsValid(boolean isValid) { this.isValid = isValid; }
        public int getType() { return type; }
        public void setType(int type) { this.type = type; }
        public int getMaxHrCustom() { return maxHrCustom; }
        public void setMaxHrCustom(int maxHrCustom) { this.maxHrCustom = maxHrCustom; }
        public int getMaxHrAuto() { return maxHrAuto; }
        public void setMaxHrAuto(int maxHrAuto) { this.maxHrAuto = maxHrAuto; }
        public int getAt() { return at; }
        public void setAt(int at) { this.at = at; }
        public int getAnt() { return ant; }
        public void setAnt(int ant) { this.ant = ant; }

        @Override
        public String toString() {
            return "HeartRate{" +
                    "isValid=" + isValid +
                    ", type=" + type +
                    ", maxHrCustom=" + maxHrCustom +
                    ", maxHrAuto=" + maxHrAuto +
                    ", at=" + at +
                    ", ant=" + ant +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HeartRateZone {

        @JsonProperty("is_valid")
        private boolean isValid;

        // Getters and Setters...
        public boolean getIsValid() { return isValid; }
        public void setIsValid(boolean isValid) { this.isValid = isValid; }

        @Override
        public String toString() {
            return "HeartRateZone{isValid=" + isValid + '}';
        }
    }

    // --- Getters and Setters for main class ---

    public HeartRate getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(HeartRate heartRate) {
        this.heartRate = heartRate;
    }

    public HeartRateZone getHeartRateZone() {
        return heartRateZone;
    }

    public void setHeartRateZone(HeartRateZone heartRateZone) {
        this.heartRateZone = heartRateZone;
    }

    public int getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }

    @Override
    public String toString() {
        return "SensingStatus{" +
                "heartRate=" + heartRate +
                ", heartRateZone=" + heartRateZone +
                ", samplingRate=" + samplingRate +
                '}';
    }
}
