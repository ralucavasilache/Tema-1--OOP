package Recommendation;

import Entities.User;
import Entities.Video;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class searchPremium {
    private final int id;
    private final User user;
    private List<Video> videos;
    private final String filtre;
    private List<Video> filtredVideos = new ArrayList<Video>();

    public searchPremium(List<Video> videos, int id, User user, String username, String filtre) {
        this.id = id;
        this.user = user;
        this.filtre = filtre;
        this.videos = videos;
    }
    public JSONObject execute(Writer fileWriter) throws IOException {
        filtreByGenre(filtre);

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
    private void filtreByGenre (String genre) {

        for(Video v : videos) {
            if(!user.getHistory().containsKey(v.getTitle()) && v.getGenres().contains(genre)) {
                filtredVideos.add(v);
            }
        }
    }
    private void ascSort() {
        Comparator<Video> comparator = (v1, v2) -> {
            if (v1.calcAvg() != v2.calcAvg()) {
                return Double.compare(v1.calcAvg(), v2.calcAvg());
            } else {
                return v1.getTitle().compareTo(v2.getTitle());
            }
        };
        Collections.sort(filtredVideos, comparator);
    }
}
