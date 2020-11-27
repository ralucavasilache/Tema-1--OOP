package queries;

import entities.Show;
import entities.User;
import fileio.Writer;
import org.json.simple.JSONObject;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class FavoriteShows {
    /**
     * Lista cu serialele din baza de date
     */
    private final List<Show> shows;
    /**
     * Id-ul actiunii
     */
    private final int id;
    /**
     * Numarul maxim de seriale care trebuie printate
     */
    private final int number;
    /**
     * Lista de filtre
     */
    private final List<List<String>> filters;
    /**
     * Tipul sortarii
     */
    private final String sortType;
    /**
     * Lista cu userii din baza de date
     */
    private final List<User> users;

    public FavoriteShows(final List<Show> shows, final int id, final int number,
                         final List<List<String>> filters, final String sortType,
                         final List<User> users) {

        this.shows = new ArrayList<>();
        this.shows.addAll(shows);
        this.id = id;
        this.number = number;
        this.filters = filters;
        this.sortType = sortType;
        this.users = users;
    }
    /**
     * Executa actiunea favorite_shows, prin apelul metodelor corespunzatoare
     * @param fileWriter, obiect Writer ce va scrie mesajul rezultat in urma actiunii
     * @return JSONObject
     */
    public JSONObject execute(final Writer fileWriter) throws IOException {
        setFavorite();
        ascsort();
        if (sortType.equals("desc")) {
            Collections.reverse(shows);
        }
        return fileWriter.writeFile(id, null, "Query result: " + filter());
    }
    /**
     * Sorteaza serialele crescator dupa numarul de aparitii in listele
     * de favorite, apoi dupa nume.
     */
    private void ascsort() {
        Comparator<Show> comparator = (s1, s2) -> {
            if (s1.getFavorite() != s2.getFavorite()) {
                return Integer.compare(s1.getFavorite(), s2.getFavorite());
            } else {
                return 0;
            }
        };
        shows.sort(comparator);
    }
    /**
     * Actualizeaza campul favorite pentru toate serialele care apar in listele
     * de favorite.
     */
    private void setFavorite() {
        for (User u : users) {
            for (String show : u.getFavoriteMovies()) {
                Show s = Utils.searchShow(shows, show);
                if (s != null) {
                    s.setFavorite(1);
                }
            }
        }
    }
    /**
     * Creeaza o lista finala cu primele "number" titluri de seriale
     * (cu favorite != 0), care trebuie printate si care respecta filtrele.
     */
    private List<String> filter() {
        int limit = 1;

        List<String> filteredShows = new ArrayList<>();
        for (Show s : shows) {
            boolean found = true;
            if (filters.get(0).get(0) != null) {
                if (!String.valueOf(s.getYear()).equals(filters.get(0).get(0))) {
                    found = false;
                }
            }
            if (filters.get(1).get(0) != null) {
                for (String genre : filters.get(1)) {
                    if (!s.getGenres().contains(genre)) {
                        found = false;
                        break;
                    }
                }
            }
            if (limit <= number && s.getFavorite() != 0 && found) {
                filteredShows.add(s.getTitle());
                limit++;
            }
        }
        return filteredShows;
    }
}
