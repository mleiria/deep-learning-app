package pt.mleiria.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Root POJO for the Myotest advanced metrics data from com.samsung.shealth.exercise.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdditionalInternalVo {

    @JsonProperty("data")
    private List<AdditionalInternalData> data;

    public List<AdditionalInternalData> getData() {
        return data;
    }

    public void setData(List<AdditionalInternalData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MyotestVo{" + "data=" + data + '}';
    }

    /**
     * Represents the main data object provided by the Myotest service.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdditionalInternalData {

        @JsonProperty("service_name")
        private String serviceName;

        @JsonProperty("version")
        private int version;

        @JsonProperty("sampling_rate")
        private int samplingRate;

        @JsonProperty("advanced_metrics")
        private List<AdvancedMetric> advancedMetrics;

        // Getters and Setters
        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }
        public int getVersion() { return version; }
        public void setVersion(int version) { this.version = version; }
        public int getSamplingRate() { return samplingRate; }
        public void setSamplingRate(int samplingRate) { this.samplingRate = samplingRate; }
        public List<AdvancedMetric> getAdvancedMetrics() { return advancedMetrics; }
        public void setAdvancedMetrics(List<AdvancedMetric> advancedMetrics) { this.advancedMetrics = advancedMetrics; }

        @Override
        public String toString() {
            return "MyotestData{" +
                    "serviceName='" + serviceName + '\'' +
                    ", version=" + version +
                    ", samplingRate=" + samplingRate +
                    ", advancedMetrics=" + advancedMetrics +
                    '}';
        }
    }

    /**
     * Represents a single advanced metric (e.g., asymmetry, stiffness).
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdvancedMetric {

        @JsonProperty("data_type")
        private String dataType;

        @JsonProperty("overall_score")
        private int overallScore;

        @JsonProperty("score_ratio")
        private List<Double> scoreRatio;

        @JsonProperty("chart_data")
        private List<ChartDataPoint> chartData;

        // Getters and Setters
        public String getDataType() { return dataType; }
        public void setDataType(String dataType) { this.dataType = dataType; }
        public int getOverallScore() { return overallScore; }
        public void setOverallScore(int overallScore) { this.overallScore = overallScore; }
        public List<Double> getScoreRatio() { return scoreRatio; }
        public void setScoreRatio(List<Double> scoreRatio) { this.scoreRatio = scoreRatio; }
        public List<ChartDataPoint> getChartData() { return chartData; }
        public void setChartData(List<ChartDataPoint> chartData) { this.chartData = chartData; }

        @Override
        public String toString() {
            return "AdvancedMetric{" +
                    "dataType='" + dataType + '\'' +
                    ", overallScore=" + overallScore +
                    ", scoreRatio=" + scoreRatio +
                    ", chartData=" + chartData +
                    '}';
        }
    }

    /**
     * Represents a single time-series data point within a chart.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChartDataPoint {

        @JsonProperty("duration")
        private long duration;

        @JsonProperty("value")
        private double value;

        @JsonProperty("score")
        private int score;

        // Getters and Setters
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
        public double getValue() { return value; }
        public void setValue(double value) { this.value = value; }
        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }

        @Override
        public String toString() {
            return "ChartDataPoint{" +
                    "duration=" + duration +
                    ", value=" + value +
                    ", score=" + score +
                    '}';
        }
    }
}