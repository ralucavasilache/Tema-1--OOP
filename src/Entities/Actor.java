package Entities;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public  class Actor {

    private String name;
    private String careerDescription;
    private ArrayList<String> filmography;
    private List<Movie> movies;
    private List<Show> shows;
    private double rating = 0;
    private Map<ActorsAwards, Integer> awards;

    public Actor(final String name, final String careerDescription,
                          final ArrayList<String> filmography,
                          final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
    }

    public double getRating() {
        return rating;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
    public int getAwardsNumber() {
        int awardsNumber = 0;
        for(Map.Entry<ActorsAwards, Integer> entry : awards.entrySet()) {
            awardsNumber += entry.getValue();
        }
        return awardsNumber;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }
    public void calculateRating() {
        double avgM = 0;
        int mNumber = 0;
        for(Movie m : movies) {

            double s = m.calcAvg();
            if(s != 0) {
                avgM += s;
                mNumber++;

            }
        }
        double avgS = 0;
        int sNumber = 0;
        for(Show s : shows) {
            double sum = s.calcAvg();
            if(sum != 0) {
                avgS += sum;
                sNumber++;
            }
        }
        if(sNumber+mNumber != 0)
            this.rating = (avgM + avgS)/(sNumber + mNumber);
    }
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public void setCareerDescription(final String careerDescription) {
        this.careerDescription = careerDescription;
    }

    @Override
    public String toString() {
        return "ActorInputData{"
                + "name='" + name + '\''
                + ", careerDescription='"
                + careerDescription + '\''
                + ", filmography=" + filmography + '}';
    }
}
