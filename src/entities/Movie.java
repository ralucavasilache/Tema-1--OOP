package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Informatii despre un film
 * Metode specifice filmului
 */
public final class Movie extends Video {
    /**
     * Durata filmului in minute
     */
    private final int duration;
    /**
     * Map pentru rating-urile primite
     * cheia - username, valoarea - rating
     */
    private final Map<String, Double> rating = new HashMap<>();

    public Movie(final String title, final ArrayList<String> cast,
                 final ArrayList<String> genres, final int year,
                 final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
    }
    /**
     * Adauga o pereche (username, rating) in map-ul de rating
     * @param movieRating, rating acordat
     * @param username, numele urilizatorului care a acordat rating-ul
     */
    public void setRating(final double movieRating, final String username) {
        this.rating.put(username, movieRating);
    }

    public int getDuration() {
        return duration;
    }
    public Map<String, Double> getRating() {
        return rating;
    }
    /**
     * Calculeaza rating-ul mediu
     * @return un double, reprezentand media rating-urilor
     */
    @Override
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
