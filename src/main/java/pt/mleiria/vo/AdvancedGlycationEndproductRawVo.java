package pt.mleiria.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AdvancedGlycationEndproductRawVo {

    @JsonProperty("score")
    private int score;

    @JsonProperty("timestamp")
    private long timestamp;

    public AdvancedGlycationEndproductRawVo() {
    }

    public AdvancedGlycationEndproductRawVo(int score, long timestamp) {
        this.score = score;
        this.timestamp = timestamp;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AdvancedGlycationEndproductRawVo that = (AdvancedGlycationEndproductRawVo) o;
        return score == that.score && timestamp == that.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(score, timestamp);
    }
}
