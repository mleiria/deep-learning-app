package pt.mleiria.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This data type from Samsung Health tracks your respiratory rate, which is the number of breaths you take per minute.
 * This is a key vital sign that is often measured during sleep to provide insights into your overall health, sleep quality, and fitness levels.
 * <p>
 * "start_time": The Unix timestamp in milliseconds indicating the beginning of the measurement period. 1746657180000
 * corresponds to Friday, July 4, 2025, at 10:13:00 AM GMT.
 * <p>
 * "end_time": The Unix timestamp in milliseconds for the end of the period. 1746657239999 corresponds to Friday, July 4, 2025, at 10:13:59.999 AM GMT.
 * <p>
 * "respiratory_rate": The core metric. This is your average respiratory rate during the specified time window, measured in breaths per minute.
 * <p>
 * Key Observations from Your Data
 * Time Interval: The difference between the end_time and start_time in each object is consistently 59999 milliseconds.
 * This shows that your respiratory rate is being measured and averaged over 60-second (1-minute) intervals.
 * <p>
 * 0.0 Values: The data begins with several entries where the respiratory_rate is 0.0. This typically means one of a few things:
 * The device was not able to get a reliable reading.
 * The device was not being worn, or not worn correctly, during this period.
 * The algorithm is calibrating or has discarded noisy/unreliable data.
 * Typical Values: After the initial zero values, the respiratory rate stabilizes in a range of approximately 16 to 18
 * breaths per minute. This is a very common and healthy respiratory rate for an adult at rest or during sleep.
 * <p>
 * What This Data Is Used For
 * <p>
 * This granular data allows you to see how your breathing rate changes throughout the night. Fluctuations can be normal,
 * but tracking the average and trends over time can be valuable. The Samsung Health app uses this data to:
 * <p>
 * Calculate your average respiratory rate for the entire sleep session.
 * <p>
 * Contribute to your overall sleep score.
 * <p>
 * Help identify potential sleep disturbances or irregularities in breathing patterns.
 * For most adults, a typical respiratory rate during sleep is between 12 and 20 breaths per minute.
 * Consistent measurements outside of this range, or significant, unusual fluctuations, could be something to discuss with a healthcare professional.
 */
public class RespiratoryRateVo {


    /**
     * The start time of the measurement in Unix milliseconds.
     */
    @JsonProperty("start_time")
    private long startTime;

    /**
     * The end time of the measurement in Unix milliseconds.
     */
    @JsonProperty("end_time")
    private long endTime;

    /**
     * The average respiratory rate in breaths per minute.
     */
    @JsonProperty("respiratory_rate")
    private double respiratoryRate;

    // Default no-argument constructor
    public RespiratoryRateVo() {
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

    public double getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(double respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    @Override
    public String toString() {
        return "RespiratoryRateData{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", respiratoryRate=" + respiratoryRate +
                '}';
    }

}
