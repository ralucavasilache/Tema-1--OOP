package Recommendation;

import Entities.User;
import Entities.Video;
import fileio.Writer;
import org.json.simple.JSONObject;
import utils.Utils;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class favoritePremium {
    private final int id;
    private final List<User> users;
    private final String username;
    private List<Video> videos;

    public favoritePremium(List<Video> videos, int id, List<User> users, String username) {
        this.id = id;
        this.users = users;
        this.username = username;
        this.videos = videos;
    }
    public JSONObject execute(Writer fileWriter) throws IOException {
        addToFavorite();
        descSort();
        User user = Utils.searchUser(users, username);
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
        Comparator<Video> comparator = (v1, v2) -> {
            if (v1.getFavorite() != v2.getFavorite()) {
                return Integer.compare(v2.getFavorite(), v1.getFavorite());
            } else {
                return videos.indexOf(v1) - videos.indexOf(v2);
            }
        };
        Collections.sort(videos, comparator);
    }
    void addToFavorite() {
        for(User u : users) {
            if(!u.getUsername().equals(username)) {
                for (String video : u.getFavoriteMovies()) {
                    Video v = Utils.searchVideo(videos, video);
                    if (v != null) {
                        v.setFavorite(1);
                    }
                }
            }
        }
    }
}
