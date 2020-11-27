package entities;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Informatii despre un actor
 * Metode specifice actorului
 */
public final class Actor {
    /**
     * Numele actorului
     */
    private final String name;
    /**
     * Descrierea carierei actorului
     */
    private final String careerDescription;
    /**
     * Titlurile filmelor/serialelor in care a jucat
     */
    private final ArrayList<String> filmography;
    /**
     * Lista filmelor in care  jucat
     */
    private List<Movie> movies;
    /**
     * Lista serialelor in care  jucat
     */
    private List<Show> shows;
    /**
     * Ratingul calculat
     */
    private double rating = 0;
    /**
     * Map pentru premiile castigate
     * cheia - numele premiului, valoarea - numarul
     */
    private final Map<ActorsAwards, Integer> awards;

    public Actor(final String name, final String careerDescription,
                          final ArrayList<String> filmography,
                          final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
    }

    public void setMovies(final List<Movie> movies) {
        this.movies = movies;
    }

    public void setShows(final List<Show> shows) {
        this.shows = shows;
    }
    /**
     * Calculeaza numarul total de premii primite
     * @return un int, numarul premiilor
     */
    public int calcAwardsNumber() {
        int awardsNumber = 0;
        for (Map.Entry<ActorsAwards, Integer> entry : awards.entrySet()) {
            awardsNumber += entry.getValue();
        }
        return awardsNumber;
    }
    /**
     * Calculeaza si seteaza ratingul
     */
    public void calculateRating() {
        double avgM = 0;
        int mNumber = 0;
         // media filmelor in care a jucat
        for (Movie m : movies) {
            double s = m.calcAvg();
            if (s != 0) {
                avgM += s;
                mNumber++;

            }
        }
        double avgS = 0;
        int sNumber = 0;
         // media serialelor in care a jucat
        for (Show s : shows) {
            double sum = s.calcAvg();
            if (sum != 0) {
                avgS += sum;
                sNumber++;
            }
        }
         // media actorului
        if (sNumber + mNumber != 0) {
            this.rating = (avgM + avgS) / (sNumber + mNumber);
        }
    }
    public String getName() {
        return name;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public double getRating() {
        return rating;
    }
}
