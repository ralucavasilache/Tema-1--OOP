package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class standard {
    private final List<Show> shows;
    private final List<Movie> movies;
    private final int id;
    private final List<User> users;
    private final String username;
    private List<Video> videos;
    public standard(List<Show> shows, List<Movie> movies, int id, List<User> users, String username) {
        this.shows = shows;
        this.movies = movies;
        this.id = id;
        this.users = users;
        this.username = username;
        videos = new ArrayList<Video>();
        videos.addAll(movies);
        videos.addAll(shows);
    }
    public JSONObject execute(Writer fileWriter) throws IOException {
        User user = searchUser(username);
        for(Video v : videos) {
            if(!user.getHistory().containsKey(v.getTitle())) {
                return fileWriter.writeFile(id, null, "StandardRecommendation result: " + v.getTitle());
            }
        }
        return fileWriter.writeFile(id, null, "StandardRecommendation cannot be applied!" );
    }
    private User searchUser(String username) {
        for(User u : users) {
            if(u.getUsername().equals(username)) {
                return  u;
            }
        }
        return null;
    }
}
