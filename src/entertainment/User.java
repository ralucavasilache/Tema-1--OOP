package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private final String username;
    private final String subscriptionType;
    private final Map<String, Integer> history;
    private final ArrayList<String> favoriteMovies;
    private final List<Movie> movies;
    private final List<Show> shows;
    private int ratingsNo = 0;

    public User(String username, String subscriptionType, Map<String,
            Integer> history, ArrayList<String> favoriteMovies, List<Movie> movies, List<Show> shows) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.history = history;
        this.favoriteMovies = favoriteMovies;
        this.movies = movies;
        this.shows = shows;
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

    public JSONObject Favorite(String video, Writer fileWriter, int id) throws IOException {
        JSONObject out = null;
        if(history.containsKey(video) && !favoriteMovies.contains(video)) {
            favoriteMovies.add(video);
            out = fileWriter.writeFile(id, null, "success -> " + video + " was added as favourite");

        } else if(history.containsKey(video) && favoriteMovies.contains(video)) {
            out = fileWriter.writeFile(id, null, "error -> " + video + " is already in favourite list");
        }
        else if (!history.containsKey(video)) {
            out = fileWriter.writeFile(id, null, "error -> " + video + " is not seen");
        }
        return out;
    }

    public JSONObject View(String video, Writer fileWriter, int id) throws IOException {
        JSONObject out = null;
        if(!history.containsKey(video)) {
            history.put(video, 1);

        }
        else {
            history.put(video, history.get(video) + 1);
        }
        out = fileWriter.writeFile(id, null, "success -> " + video
                                    +" was viewed with total views of " + history.get(video));
        return out;
    }

    public int getRatingsNo() {
        return ratingsNo;
    }

    public JSONObject setMovieRating(Movie movie, double rating, Writer fileWriter, int id) throws IOException {
        JSONObject out = null;
        if(history.containsKey(movie.getTitle())) {
            //verifica map ratings din movie
            if(!movie.getRating().containsKey(username)) {
                movie.setRating(rating, username);
                ratingsNo++;
                return fileWriter.writeFile(id, null, "success -> " + movie.getTitle() + " was rated with "
                                            + rating + " by " + this.getUsername());
            } else if(movie.getRating().containsKey(username)) {
                out = fileWriter.writeFile(id, null, "error -> " + movie.getTitle()
                                            + " has been already rated");
            }
        } else if(!history.containsKey(movie.getTitle())) {
//            System.out.println(id + "--------" + movie.getTitle());
            out = fileWriter.writeFile(id, null, "error -> " + movie.getTitle() + " is not seen");
            if(out != null) {
//                System.out.println("Nu este null");
            }
        }
        return out;
    }
    public JSONObject setShowRating(Show show, double rating, Writer fileWriter, int season, int id) throws IOException {
        JSONObject out = null;
        if(history.containsKey(show.getTitle())) {
            //System.out.println(show);
            if (!show.getSeasons().get(season - 1).getRatings().containsKey(username)) {
                show.setRating(rating, season, username);
                out = fileWriter.writeFile(id, null, "success -> " + show.getTitle() + " was rated with "
                                            + rating + " by " + this.getUsername());
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", subscriptionType='" + subscriptionType + '\'' +
                ", history=" + history +
                ", favoriteMovies=" + favoriteMovies +
                ", ratingsNo=" + ratingsNo +
                '}';
    }
}
