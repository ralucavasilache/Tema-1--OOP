package entities;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
/**
 * Informatii despre un utilizator
 * Metode specifice utilizatorului
 */
public final class User {
    /**
     * Numele utilizatorului
     */
    private final String username;
    /**
     * Tipul de abonament
     */
    private final String subscriptionType;
    /**
     * Istoricul de video vizualizate
     */
    private final Map<String, Integer> history;
    /**
     * Video favorite
     */
    private final ArrayList<String> favoriteMovies;
    /**
     * Numarul de rating-uri acordate
     */
    private int ratingsNo = 0;

    public User(final String username, final String subscriptionType, final Map<String,
                Integer> history, final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.history = history;
        this.favoriteMovies = favoriteMovies;
    }
    /**
     * Adauga un video in lista de favorite
     * @param video, numele videoclipului care trebuie adaugat
     * @param fileWriter, un obiect Writer
     * @param id, id-ul actiunii
     * @return out, JSONObject continand mesajul afisat in urma actiunii
     */
    public JSONObject favorite(final String video, final Writer fileWriter, final int id)
                                throws IOException {
        JSONObject out = null;
        if (history.containsKey(video) && !favoriteMovies.contains(video)) {
            favoriteMovies.add(video);
            out = fileWriter.writeFile(id, null, "success -> " + video + " was added as favourite");

        } else if (favoriteMovies.contains(video)) {
            out = fileWriter.writeFile(id, null, "error -> "
                                        + video + " is already in favourite list");
        } else if (!history.containsKey(video)) {
            out = fileWriter.writeFile(id, null, "error -> " + video + " is not seen");
        }
        return out;
    }
    /**
     * Adauga un video in istoric
     * @param video, numele videoclipului care trebuie adaugat
     * @param fileWriter, un obiect Writer
     * @param id, id-ul actiunii
     * @return out, JSONObject continand mesajul afisat in urma actiunii
     */
    public JSONObject view(final String video, final Writer fileWriter, final int id)
                            throws IOException {
        JSONObject out;
        if (!history.containsKey(video)) {
            history.put(video, 1);
        } else {
            history.put(video, history.get(video) + 1);
        }
        out = fileWriter.writeFile(id, null, "success -> " + video
                                    + " was viewed with total views of " + history.get(video));
        return out;
    }
    /**
     * Acorda rating unui film
     * @param movie, numele filmulul care primeste rating
     * @param fileWriter, un obiect Writer
     * @param id, id-ul actiunii
     * @return out, JSONObject continand mesajul afisat in urma actiunii
     */
    public JSONObject setMovieRating(final Movie movie, final double rating,
                                     final Writer fileWriter, final int id) throws IOException {
        JSONObject out = null;
        if (history.containsKey(movie.getTitle())) {

            if (!movie.getRating().containsKey(username)) {
                movie.setRating(rating, username);
                ratingsNo++;
                return fileWriter.writeFile(id, null, "success -> " + movie.getTitle()
                                            + " was rated with " + rating + " by " + username);

            } else if (movie.getRating().containsKey(username)) {
                out = fileWriter.writeFile(id, null, "error -> " + movie.getTitle()
                                            + " has been already rated");
            }

        } else if (!history.containsKey(movie.getTitle())) {
            out = fileWriter.writeFile(id, null, "error -> " + movie.getTitle() + " is not seen");
        }
        return out;
    }
    /**
     * Acorda rating unui serial
     * @param show, numele serialului care primeste rating
     * @param fileWriter, un obiect Writer
     * @param id, id-ul actiunii
     * @return out, JSONObject continand mesajul afisat in urma actiunii
     */
    public JSONObject setShowRating(final Show show, final double rating, final Writer fileWriter,
                                    final int season, final int id) throws IOException {
        JSONObject out;
        if (history.containsKey(show.getTitle())) {
            if (!show.getSeasons().get(season - 1).getRatings().containsKey(username)) {
                show.addSeasonRating(rating, season, username);
                out = fileWriter.writeFile(id, null, "success -> " + show.getTitle()
                                            + " was rated with " + rating + " by " + username);
                ratingsNo++;
            } else {
                return fileWriter.writeFile(id, null, "error -> " + show.getTitle()
                    + " has been already rated");
            }
        } else {
            out = fileWriter.writeFile(id, null, "error -> " + show.getTitle() + " is not seen");
        }
        return out;
    }
    public String getUsername() {
        return username;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public int getRatingsNo() {
        return ratingsNo;
    }

}
