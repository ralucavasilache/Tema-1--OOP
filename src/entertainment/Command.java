package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

public class Command {
    private final String type;
    private final String userName;
    private final String title;
    private final double rating;
    private  final int seasonNo;
    private final int id;
    private final List<User> users;
    private final List<Movie> movies;
    private final List<Show> shows;
    private final Writer fileWriter;

    public Command (String type, String userName, String title, double rating, int seasonNo, int id,
                    List<User> users, List<Movie> movies, List<Show> shows, Writer fileWriter) {
        this.type = type;
        this.userName = userName;
        this.title = title;
        this.rating = rating;
        this.seasonNo = seasonNo;
        this.id = id;
        this.users = users;
        this.movies = movies;
        this.shows = shows;
        this.fileWriter = fileWriter;

    }

    public JSONObject execute () throws IOException {
        JSONObject ret = null;
        if(this.type.equals("favorite")) {
           ret =  this.favorite();
        }
        else if (this.type.equals("view")) {
            ret = this.view();
        }
        else if(this.type.equals("rating")){
            ret = this.giveRating();
        }
        return ret;
    }

    private JSONObject favorite () throws IOException {
        User u = searchUser();
        JSONObject out = u.Favorite(this.title, fileWriter, this.id);
        return out;
    }

    private JSONObject view () throws IOException {
        User u = searchUser();
        JSONObject out = u.View(this.title, fileWriter, this.id);
        return out;
    }

    private JSONObject giveRating () throws IOException {
        JSONObject out = null;
        User u = searchUser();

        if(searchMovie() != null) {
            Movie m = searchMovie();

            out = u.setMovieRating(m, this.rating, fileWriter, id);
        }
        else if (searchShow() != null) {
            Show s = searchShow();
            out = u.setShowRating(s, rating, fileWriter, seasonNo, id);
        }
        return out;
    }

    private User searchUser() {
        User user = null;
        for (User u : users) {
            if (u.getUsername().equals(userName)) {
                user = u;
                break;
            }
        }
        return user;
    }

    private Movie searchMovie() {
        Movie movie = null;
        for (Movie m : movies) {
            if (m.getTitle().equals(title)) {
                movie = m;
                break;
            }
        }
        return movie;
    }

    private Show searchShow() {
        Show show = null;
        for (Show s : shows) {
            if (s.getTitle().equals(title)) {
                show = s;
                break;
            }
        }
        return show;
    }
}
