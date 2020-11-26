package commands;

import entities.Movie;
import entities.Show;
import entities.User;
import fileio.Writer;
import org.json.simple.JSONObject;
import utils.Utils;

import java.io.IOException;
import java.util.List;

public class Command {
    private final String type;
    private final String title;
    private final double rating;
    private final int seasonNo;
    private final int id;
    private final User user;
    private final List<Movie> movies;
    private final List<Show> shows;
    private final Writer fileWriter;

    public Command(final String type, final User user, final String title, final double rating,
                   final int seasonNo, final int id, final List<Movie> movies,
                   final List<Show> shows, final Writer fileWriter) {
        this.type = type;
        this.title = title;
        this.rating = rating;
        this.seasonNo = seasonNo;
        this.id = id;
        this.user = user;
        this.movies = movies;
        this.shows = shows;
        this.fileWriter = fileWriter;

    }
    /**
     * Executa, dupa caz, actiunile favorite,
     * view si rating, prin apelul metodelor specifice
     * @return un JSONObject
     */
    public JSONObject execute() throws IOException {
        JSONObject ret = null;
        if (this.type.equals("favorite")) {
           ret =  this.favorite();
        } else if (this.type.equals("view")) {
            ret = this.view();
        } else if (this.type.equals("rating")) {
            ret = this.giveRating();
        }
        return ret;
    }

    private JSONObject favorite() throws IOException {
        JSONObject out = user.favorite(this.title, fileWriter, this.id);
        return out;
    }

    private JSONObject view() throws IOException {
        JSONObject out = user.view(this.title, fileWriter, this.id);
        return out;
    }

    private JSONObject giveRating() throws IOException {
        JSONObject out = null;

        if (Utils.searchMovie(movies, title) != null) {
            Movie m = Utils.searchMovie(movies, title);
            out = user.setMovieRating(m, this.rating, fileWriter, id);
        } else if (Utils.searchShow(shows, title) != null) {
            Show s = Utils.searchShow(shows, title);
            out = user.setShowRating(s, rating, fileWriter, seasonNo, id);
        }
        return out;
    }
}
