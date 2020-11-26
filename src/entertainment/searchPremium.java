package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class searchPremium {
    private final int id;
    private final List<User> users;
    private final String username;
    private List<Video> videos;
    private final String filtre;
    private List<Video> filtredVideos = new ArrayList<Video>();

    public searchPremium(List<Video> videos, int id, List<User> users, String username, String filtre) {
        this.id = id;
        this.users = users;
        this.username = username;
        this.filtre = filtre;
        this.videos = videos;
    }
    public JSONObject execute(Writer fileWriter) throws IOException {
        filtreByGenre(filtre);

        User user = searchUser(username);

        if (!user.getSubscriptionType().equals("BASIC") && filtredVideos.size() != 0) {
            ascSort();
            List<String> videostoprint = new ArrayList<String>();
            for(Video v  : filtredVideos) {
                videostoprint.add(v.getTitle());
            }
            return fileWriter.writeFile(id, null, "SearchRecommendation result: " + videostoprint);
        } else {
            return fileWriter.writeFile(id, null, "SearchRecommendation cannot be applied!");

        }
    }
    private User searchUser(String username) {
        for(User u : users) {
            if(u.getUsername().equals(username)) {
                return  u;
            }
        }
        return null;
    }
    private void filtreByGenre (String genre) {

        User user = searchUser(username);
        for(Video v : videos) {
            if(!user.getHistory().containsKey(v.getTitle()) && v.getGenres().contains(genre)) {
                filtredVideos.add(v);
            }
        }
    }
    private void ascSort() {
        Comparator<Video> comparator = new Comparator<Video>() {
            @Override
            public int compare(final Video v1, final Video v2) {
                if (v1.calcAvg() != v2.calcAvg()) {
                    return Double.compare(v1.calcAvg(), v2.calcAvg());
                } else {
                    return v1.getTitle().compareTo(v2.getTitle());
                }
            }
        };
        Collections.sort(filtredVideos, comparator);
    }
}
