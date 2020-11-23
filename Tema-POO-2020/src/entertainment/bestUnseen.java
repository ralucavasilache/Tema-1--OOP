package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;

public class bestUnseen {
    private final List<Show> shows;
    private final List<Movie> movies;
    private final int id;
    private final List<User> users;
    private final String username;
    private List<Video> videos;

    public bestUnseen(List<Show> shows, List<Movie> movies, int id, List<User> users, String username) {
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
        descSort();
        User user = searchUser(username);
        for(Video v : videos) {
            if(!user.getHistory().containsKey(v.getTitle())) {

                return fileWriter.writeFile(id, null, "BestRatedUnseenRecommendation result: " + v.getTitle());
            }
        }
        return fileWriter.writeFile(id, null, "BestRatedUnseenRecommendation cannot be applied!");
    }
    private void descSort() {
        Comparator<Video> comparator = new Comparator<Video>() {
            @Override
            public int compare(final Video v1, final Video v2) {
                if (v1.calcAvg() != v2.calcAvg()) {
                    return Double.compare(v2.calcAvg(), v1.calcAvg());
                } else {
                    return videos.indexOf(v1) - videos.indexOf(v2);
                }
            }
        };

        Collections.sort(videos, comparator);
//        for(Video v : videos) {
//            System.out.println(v.getTitle() + "   " + v.calcAvg());
//        }
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
