package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Movie extends Video {

    private final int duration;
    private int views = 0;
    private int favorite = 0;

    public int getViews() {
        return views;
    }

    public void setViews(final int views) {
        this.views += views;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(final int favorite) {
        this.favorite += favorite;
    }

    private final Map<String, Double> rating = new HashMap<String, Double>();

    public Movie(final String title, final ArrayList<String> cast,
                 final ArrayList<String> genres, final int year,
                 final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
    }

    public void setRating(final double movieRating, final String user) {
        this.rating.put(user, movieRating);
    }

    public int getDuration() {
        return duration;
    }
    public Map<String, Double> getRating() {
        return rating;
    }

    public double calcAvg() {
        double avg = 0;
        for (double r : rating.values()) {
            avg += r;

        }
        if (avg != 0) {
            avg = avg / rating.size();
        }
        return avg;
    }
}
