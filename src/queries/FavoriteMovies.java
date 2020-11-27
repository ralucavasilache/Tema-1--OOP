package queries;

import entities.Movie;
import entities.User;
import fileio.Writer;
import org.json.simple.JSONObject;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class FavoriteMovies {
    /**
     * Lista cu filmele din baza de date
     */
    private final List<Movie> movies;
    /**
     * Id-ul actiunii
     */
    private final int id;
    /**
     * Numarul maxim de filme care trebuie printate
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

    public FavoriteMovies(final List<Movie> movie, final int id, final int number,
                          final List<List<String>> filters, final String sortType,
                          final List<User> users) {

        this.movies = new ArrayList<>();
        this.movies.addAll(movie);
        this.id = id;
        this.number = number;
        this.filters = filters;
        this.sortType = sortType;
        this.users = users;
    }
    /**
     * Executa actiunea favorite_movies, prin apelul metodelor corespunzatoare
     * @param fileWriter, obiect Writer ce va scrie mesajul rezultat in urma actiunii
     * @return JSONObject
     */
    public JSONObject execute(final Writer fileWriter) throws IOException {
        setFavorite();
        ascsort();
        if (sortType.equals("desc")) {
            Collections.reverse(movies);
        }
        return fileWriter.writeFile(id, null, "Query result: " + filter());
    }
    /**
     * Sorteaza filmele crescator dupa numarul de aparitii in listele
     * de favorite, apoi dupa nume.
     */
    private void ascsort() {
        Comparator<Movie> comparator = (m1, m2) -> {
            if (m1.getFavorite() != m2.getFavorite()) {
                return Integer.compare(m1.getFavorite(), m2.getFavorite());
            } else {
                return m1.getTitle().compareTo(m2.getTitle());
            }
        };
        movies.sort(comparator);
    }
    /**
     * Actualizeaza campul favorite pentru toate filmele care apar in listele
     * de favorite.
     */
    private void setFavorite() {
        for (User u : users) {
            for (String movie : u.getFavoriteMovies()) {
                Movie m = Utils.searchMovie(movies, movie);
                if (m != null) {
                    m.setFavorite(1);
                }
            }
        }
    }
    /**
     * Creeaza o lista finala cu primele "number" titluri de filme
     * (cu favorite != 0), care trebuie printate si care respecta filtrele.
     */
    private List<String> filter() {
        int limit = 1;

        List<String> filteredMovies = new ArrayList<>();
        for (Movie m : movies) {
            boolean found = true;
            if (filters.get(0).get(0) != null) {
                if (!String.valueOf(m.getYear()).equals(filters.get(0).get(0))) {
                    found = false;
                }
            }
            if (filters.get(1).get(0) != null) {
                for (String genre : filters.get(1)) {
                    if (!m.getGenres().contains(genre)) {
                        found = false;
                        break;
                    }
                }
            }
            if (limit <= number && m.getFavorite() != 0 && found) {
                filteredMovies.add(m.getTitle());
                limit++;
            }
        }
        return filteredMovies;
    }
}
