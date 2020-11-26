package recommendation;

import entities.User;
import entities.Video;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchPremium {
    private final int id;
    private final User user;
    private List<Video> videos;
    private final String filter;
    private List<Video> filtredVideos = new ArrayList<Video>();

    public SearchPremium(final List<Video> videos, final int id,
                         final User user, final String filter) {
        this.id = id;
        this.user = user;
        this.filter = filter;
        this.videos = videos;
    }
    public JSONObject execute(final Writer fileWriter) throws IOException {
        filtreByGenre(filter);

        if (!user.getSubscriptionType().equals("BASIC") && filtredVideos.size() != 0) {
            ascSort();
            List<String> videosToPrint = new ArrayList<String>();
            for (Video v  : filtredVideos) {
                videosToPrint.add(v.getTitle());
            }
            return fileWriter.writeFile(id, null, "SearchRecommendation result: " + videosToPrint);
        } else {
            return fileWriter.writeFile(id, null, "SearchRecommendation cannot be applied!");

        }
    }
    private void filtreByGenre(final String genre) {

        for (Video v : videos) {
            if (!user.getHistory().containsKey(v.getTitle()) && v.getGenres().contains(genre)) {
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
