package Commands;

import Entities.Movie;
import Entities.Show;
import Entities.User;
import fileio.Writer;
import org.json.simple.JSONObject;
import utils.Utils;

import java.io.IOException;
import java.util.List;

public class Command {
    private final String type;
    private final String title;
    private final double rating;
    private  final int seasonNo;
    private final int id;
    private final User user;
    private final List<Movie> movies;
    private final List<Show> shows;
    private final Writer fileWriter;

    public Command (String type, User user, String title, double rating, int seasonNo, int id,
                    List<Movie> movies, List<Show> shows, Writer fileWriter) {
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
        JSONObject out = user.Favorite(this.title, fileWriter, this.id);
        return out;
    }

    private JSONObject view () throws IOException {
        JSONObject out = user.View(this.title, fileWriter, this.id);
        return out;
    }

    private JSONObject giveRating () throws IOException {
        JSONObject out = null;

        if(Utils.searchMovie(movies, title) != null) {
            Movie m = Utils.searchMovie(movies, title);
            out = user.setMovieRating(m, this.rating, fileWriter, id);
        }
        else if (Utils.searchShow(shows, title) != null) {
            Show s = Utils.searchShow(shows, title);
            out = user.setShowRating(s, rating, fileWriter, seasonNo, id);
        }
        return out;
    }
}
