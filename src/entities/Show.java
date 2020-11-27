package entities;

import entertainment.Season;

import java.util.ArrayList;
/**
 * Informatii despre un serial
 * Metode specifice serialului
 */
public final class Show extends Video {
    /**
     * Numarul de sezoane
     */
    private final int numberOfSeasons;
    /**
     * Lista de sezoane
     */
    private final ArrayList<Season> seasons;
    /**
     * Durata tuturor sezoanelor, in minute
     */
    private int duration;

    public Show(final String title, final ArrayList<String> cast,
                final ArrayList<String> genres,
                final int numberOfSeasons, final ArrayList<Season> seasons,
                final int year) {

        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
        setDuration();
    }
    /**
     * Adauga unui sezon un rating nou
     * @param rating, rating-ul acordat
     * @param season, numarul sezonului
     * @param username, numele utilizatorului care a acordat rating-ul
     */
    public void addSeasonRating(final double rating, final int season, final String username) {
       if (season <= numberOfSeasons) {
           seasons.get(season - 1).addRating(rating, username);
       }
    }
    /**
     * Calculeaza si seteaza durata serialului,
     * aceasta fiind egala cu suma duratelor sezoanelor
     */
    private void setDuration() {
        for (Season s : seasons) {
            this.duration += s.getDuration();
        }
    }

    public int getDuration() {
        return duration;
    }

    public int getNumberSeason() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }
    /**
     * Calculeaza rating-ul mediu
     * @return un double, reprezentand media rating-urilor
     */
    @Override
    public double calcAvg() {
        double avg = 0;
        /**
         * suma rating-urilor sezoanelor / numarul sezoanelor
         */
        for (Season s: seasons) {
            avg += s.calcAvg();
        }
        if (avg != 0) {
            avg = avg / numberOfSeasons;
        }
        return avg;
    }
}
