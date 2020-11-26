package recommendation;

import entities.User;
import entities.Video;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BestUnseen {
    private final int id;
    private final User user;
    private List<Video> videos;

    public BestUnseen(final List<Video> videos, final int id, final User user) {
        this.id = id;
        this.user = user;
        this.videos = videos;
    }
    public JSONObject execute(final Writer fileWriter) throws IOException {
        descSort();
        for (Video v : videos) {
            if (!user.getHistory().containsKey(v.getTitle())) {

                return fileWriter.writeFile(id, null, "BestRatedUnseenRecommendation result: "
                                            + v.getTitle());
            }
        }
        return fileWriter.writeFile(id, null, "BestRatedUnseenRecommendation cannot be applied!");
    }
    private void descSort() {
        Comparator<Video> comparator = (v1, v2) -> {
            if (v1.calcAvg() != v2.calcAvg()) {
                return Double.compare(v2.calcAvg(), v1.calcAvg());
            } else {
                return videos.indexOf(v1) - videos.indexOf(v2);
            }
        };
        Collections.sort(videos, comparator);
    }
}
