package recommendation;

import entities.User;
import entities.Video;
import fileio.Writer;
import org.json.simple.JSONObject;
import utils.Utils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Popular {
    /**
     * Id-ul actiunii
     */
    private final int id;
    /**
     * Lista cu utilizatorii din baza de date
     */
    private final List<User> users;
    /**
     * Numele utilizatorului care face actiunea
     */
    private final User user;
    /**
     * Lista de videoclipuri din baza de date
     */
    private final List<Video> videos;
    /**
     * Map, cheia - genul, valoarea - numarul de video care apartin acelui gen
     */
    private final Map<String, Integer> genres = new HashMap<>();
    /**
     * Array cu toate genurile care pot fi intalnite
     */
    private final String[] genreEnum = {"TV Movie", "Drama", "Fantasy", "Comedy", "Family", "War",
            "Sci-Fi & Fantasy", "Crime", "Animation", "Science Fiction",
            "Action", "Horror", "Mystery", "Western", "Adventure",
            "Action & Adventure", "Romance", "Thriller", "Kids", "History"};
    /**
     * Lista cu genurile sortate in functie de popularitate
     */
    private List<String> genresRanking;

    public Popular(final List<Video> videos, final int id,
                   final List<User> users, final User user) {

        this.id = id;
        this.users = users;
        this.user = user;
        this.videos = videos;
    }
    /**
     * Executa recomandarea popular, prin apelul metodelor corespunzatoare
     * @param fileWriter, obiect Writer ce va scrie mesajul rezultat in urma actiunii
     * @return JSONObject
     */
    public JSONObject execute(final Writer fileWriter) throws IOException {
        viewVideo();
        setGenres();
        sortByPopularity();
        if (user.getSubscriptionType().equals("BASIC") || findUnseen() == null) {
            return fileWriter.writeFile(id, null, "PopularRecommendation cannot be applied!");
        } else {
            return fileWriter.writeFile(id, null, "PopularRecommendation result: " + findUnseen());
        }
    }
    /**
     * Actualizeaza campul view pentru toate filmele care apar istoric la useri.
     */
    private void viewVideo() {
        for (User u : users) {
            for (Map.Entry<String, Integer> entry : u.getHistory().entrySet()) {
                Video v = Utils.searchVideo(videos, entry.getKey());
                if (v != null) {
                    v.setViews(entry.getValue());
                }
            }
        }
    }
    /**
     * Calculeaza numarul de video din fiecare gen si actualizeaza valoarea
     * din map
     */
    private void setGenres() {
        for (String genre : genreEnum) {
            genres.put(genre, 0);
        }
        for (Video v : videos) {
            for (String videoGenre : v.getGenres()) {
                genres.put(videoGenre, genres.get(videoGenre) + v.getViews());
            }
        }
    }
    /**
     * Gaseste primul video nevizualizat din cel mai popular gen
     * @return numele videoclipului gasit
     */
    private String findUnseen() {
        int i = 0;
        while (i < genresRanking.size()) {
            for (Video v : videos) {
                boolean found = user.getHistory().containsKey(v.getTitle());
                if (!found && v.getGenres().contains(genresRanking.get(i))) {
                    return v.getTitle();
                }
            }
            i++;
        }
        return null;
    }
    /**
     * Sorteaza map-ul pe baza popularitatii genurilor si salveaza lista
     * de genuri sortate in genresRanking
     */
    private void sortByPopularity() {
        genresRanking = genres.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
            Collections.reverse(genresRanking);
    }
}
