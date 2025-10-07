package pt.mleiria.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The com.samsung.health.movement data type is designed to provide a granular, time-series log of your physical movement as
 * captured by your Samsung device's sensors (likely the accelerometer).
 * <p>
 * Instead of just logging specific activities like "running" or "walking," this data provides a continuous,
 * moment-by-moment score of your movement intensity. The JSON you've shared is an array of these measurements, where each
 * object represents a distinct time interval.
 * <p>
 * Time Interval: If you calculate the difference between the end_time and start_time for any object in the
 * array (1758441659999 - 1758441600000), you get 59999 milliseconds. This means your movement data is being aggregated into 60-second (1-minute) intervals.
 * <p>
 * Interpreting activity_level: This value is a proprietary score from Samsung Health that quantifies movement.
 * It does not have a standard public unit (like calories or METs), but it directly correlates with motion intensity.
 * <p>
 * Low Values (e.g., 0.193, 0.216, 5.45...E-4): These numbers are very close to zero and indicate periods of almost
 * complete stillness, such as when you are sleeping, lying down, or sitting motionless at a desk.
 * <p>
 * Moderate Values (e.g., 10.37, 19.53, 49.60): These likely represent light to moderate activity, such as slow walking,
 * household chores, or general moving around.
 * <p>
 * High Values (e.g., 125.89, 200.64, 211.25): These scores signify periods of high-intensity movement. The sequence in
 * your data from 1758442920000 to 1758443279999 shows a clear 6-minute burst of vigorous activity, which could correspond to running, a brisk walk, or an exercise session.
 */
public class MovementVo {
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
     * A numerical score representing the intensity of physical movement.
     */
    @JsonProperty("activity_level")
    private double activityLevel;

    // Default no-argument constructor
    public MovementVo() {
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

    public double getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(double activityLevel) {
        this.activityLevel = activityLevel;
    }

    @Override
    public String toString() {
        return "MovementData{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", activityLevel=" + activityLevel +
                '}';
    }
}
