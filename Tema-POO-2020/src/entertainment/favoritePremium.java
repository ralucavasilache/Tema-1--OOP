package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class favoritePremium {
    private final List<Show> shows;
    private final List<Movie> movies;
    private final int id;
    private final List<User> users;
    private final String username;
    private List<Video> videos;

    public favoritePremium(List<Show> shows, List<Movie> movies, int id, List<User> users, String username) {
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
        addToFavorite();
        descSort();
        User user = searchUser(username);
        if (user.getSubscriptionType().equals("BASIC")) {
            return fileWriter.writeFile(id, null, "FavoriteRecommendation cannot be applied!");
        } else {
            for (Video v : videos) {
                if (!user.getHistory().containsKey(v.getTitle()) && v.getFavorite()!= 0) {

                    return fileWriter.writeFile(id, null, "FavoriteRecommendation result: " + v.getTitle());
                }
            }
            return fileWriter.writeFile(id, null, "FavoriteRecommendation cannot be applied!");
        }
    }
    private void descSort() {
        Comparator<Video> comparator = new Comparator<Video>() {
            @Override
            public int compare(final Video v1, final Video v2) {
                if (v1.getFavorite() != v2.getFavorite()) {
                    return Double.compare(v2.getFavorite(), v1.getFavorite());
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
    void addToFavorite() {
        for(User u : users) {
            if(!u.getUsername().equals(username)) {
                for (String video : u.getFavoriteMovies()) {
                    Video v = searchVideo(video);
                    if (v != null) {
                        v.setFavorite(1);
                    }
                }
            }
        }
    }
    public Video searchVideo(String title) {
        for(Video v : videos) {
            if(v.getTitle().equals(title)) {
                return v;
            }
        }
        return null;
    }
}
