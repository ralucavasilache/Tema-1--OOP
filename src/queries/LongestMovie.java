package queries;

import entities.Movie;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class LongestMovie {
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
    private  final int number;
    /**
     * Lista de filtre
     */
    private final List<List<String>> filters;
    /**
     * Tipul sortarii
     */
    private final String sortType;

    public LongestMovie(final List<Movie> movie, final int id, final int number,
                        final List<List<String>> filters, final String sortType) {

        this.movies = new ArrayList<>();
        this.movies.addAll(movie);
        this.id = id;
        this.number = number;
        this.filters = filters;
        this.sortType = sortType;
    }
    /**
     * Executa actiunea longest_movie, prin apelul metodelor corespunzatoare
     * @param fileWriter, obiect Writer ce va scrie mesajul rezultat in urma actiunii
     * @return JSONObject
     */
    public JSONObject execute(final Writer fileWriter) throws IOException {
        ascsort();
        if (sortType.equals("desc")) {
            Collections.reverse(movies);
        }
        return fileWriter.writeFile(id, null, "Query result: " + filter());
    }
    /**
     * Sorteaza filmele crescator dupa durata, apoi dupa nume.
     */
    private void ascsort() {
        Comparator<Movie> comparator = (m1, m2) -> {
            if (m1.getDuration() != m2.getDuration()) {
                return m1.getDuration() - m2.getDuration();
            } else {
                return m1.getTitle().compareTo(m2.getTitle());
            }
        };
        movies.sort(comparator);

    }
    /**
     * Creeaza o lista finala cu primele "number" titluri de filme
     * (cu duration != 0), care trebuie printate si care respecta filtrele.
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
            if (limit <= number && m.getDuration() != 0 && found) {
                filteredMovies.add(m.getTitle());
                limit++;
            }
        }
        return filteredMovies;
    }
}
