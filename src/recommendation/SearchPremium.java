package recommendation;

import entities.User;
import entities.Video;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SearchPremium {
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
    /**
     * Lista de filtre
     */
    private final String filter;
    /**
     * Lista cu video care trebuie printate
     */
    private final List<Video> filtredVideos = new ArrayList<>();

    public SearchPremium(final List<Video> videos, final int id,
                         final User user, final String filter) {

        this.id = id;
        this.user = user;
        this.filter = filter;
        this.videos = videos;
    }
    /**
     * Executa recomandarea search, prin apelul metodelor corespunzatoare
     * @param fileWriter, obiect Writer ce va scrie mesajul rezultat in urma actiunii
     * @return JSONObject
     */
    public JSONObject execute(final Writer fileWriter) throws IOException {
        filterByGenre(filter);

        if (!user.getSubscriptionType().equals("BASIC") && filtredVideos.size() != 0) {
            ascSort();
            List<String> videosToPrint = new ArrayList<>();
            for (Video v  : filtredVideos) {
                videosToPrint.add(v.getTitle());
            }
            return fileWriter.writeFile(id, null, "SearchRecommendation result: " + videosToPrint);
        } else {
            return fileWriter.writeFile(id, null, "SearchRecommendation cannot be applied!");

        }
    }
    /**
     * Adauga in filteredVideos doar videoclipurile care respecta filtrul
     */
    private void filterByGenre(final String genre) {

        for (Video v : videos) {
            if (!user.getHistory().containsKey(v.getTitle()) && v.getGenres().contains(genre)) {
                filtredVideos.add(v);
            }
        }
    }
    /**
     * Sorteaza videoclipurile crescator dupa rating, apoi dupa nume.
     */
    private void ascSort() {
        Comparator<Video> comparator = (v1, v2) -> {
            if (v1.calcAvg() != v2.calcAvg()) {
                return Double.compare(v1.calcAvg(), v2.calcAvg());
            } else {
                return v1.getTitle().compareTo(v2.getTitle());
            }
        };
        filtredVideos.sort(comparator);
    }
}
