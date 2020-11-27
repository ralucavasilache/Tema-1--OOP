package recommendation;

import entities.User;
import entities.Video;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public final class BestUnseen {
    /**
     * Id-ul actiunii
     */
    private final int id;
    /**
     * Numele utilizatorului care face actiunea
     */
    private final User user;
    /**
     * Lista de videoclipuri din baza de date
     */
    private final List<Video> videos;

    public BestUnseen(final List<Video> videos, final int id, final User user) {
        this.id = id;
        this.user = user;
        this.videos = videos;
    }
    /**
     * Executa actiunea best_unseen, prin apelul metodelor corespunzatoare
     * @param fileWriter, obiect Writer ce va scrie mesajul rezultat in urma actiunii
     * @return JSONObject
     */
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
    /**
     * Sorteaza video descrescator dupa rating, apoi dupa aparitia in baza de date.
     */
    private void descSort() {
        Comparator<Video> comparator = (v1, v2) -> {
            if (v1.calcAvg() != v2.calcAvg()) {
                return Double.compare(v2.calcAvg(), v1.calcAvg());
            } else {
                return videos.indexOf(v1) - videos.indexOf(v2);
            }
        };
        videos.sort(comparator);
    }
}
