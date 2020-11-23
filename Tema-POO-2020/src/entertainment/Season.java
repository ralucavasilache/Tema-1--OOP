package entertainment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Information about a season of a tv show
 * <p>
 * DO NOT MODIFY
 */
public final class Season {
    /**
     * Number of current season
     */
    private final int currentSeason;
    /**
     * Duration in minutes of a season
     */
    private int duration;
    /**
     * List of ratings for each season
     */
    private Map<String, Double> ratings;

    public Season(final int currentSeason, final int duration) {
        this.currentSeason = currentSeason;
        this.duration = duration;
        this.ratings = new HashMap<String, Double>();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public Map<String, Double> getRatings() {
        return ratings;
    }

    public void setRatings(final Map<String, Double> ratings) {
        this.ratings = ratings;
    }

    public void addRating(double rating, String user) {
        ratings.put(user, rating);
    }
    public double calcAvg() {
        double avg = 0;
        for(double r : ratings.values()) {
            avg += r;
        }
        if(avg != 0) {
            avg = avg/ratings.size();
        }
        return  avg;
    }
    @Override
    public String toString() {
        return "Season{" +
                "currentSeason=" + currentSeason +
                ", duration=" + duration +
                ", ratings=" + ratings +
                '}';
    }
}

