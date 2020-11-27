package queries;

import entities.Show;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class LongestShow {
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

    public LongestShow(final List<Show> show, final int id, final int number,
                       final List<List<String>> filters, final String sortType) {

        this.shows = new ArrayList<>();
        this.shows.addAll(show);
        this.id = id;
        this.number = number;
        this.filters = filters;
        this.sortType = sortType;
    }
    /**
     * Executa actiunea longest_show, prin apelul metodelor corespunzatoare
     * @param fileWriter, obiect Writer ce va scrie mesajul rezultat in urma actiunii
     * @return JSONObject
     */
    public JSONObject execute(final Writer fileWriter) throws IOException {
        ascsort();
        if (sortType.equals("desc")) {
            Collections.reverse(shows);
        }
        return fileWriter.writeFile(id, null, "Query result: " + filter());
    }
    /**
     * Sorteaza serialele crescator dupa durata, apoi dupa nume.
     */
    private void ascsort() {
        Comparator<Show> comparator = (s1, s2) -> {
            if (s1.getDuration() != s2.getDuration()) {
                return s1.getDuration() - s2.getDuration();
            } else {
                return s1.getTitle().compareTo(s2.getTitle());
            }
        };
        shows.sort(comparator);
    }
    /**
     * Creeaza o lista finala cu primele "number" titluri de seriale
     * (cu duration != 0), care trebuie printate si care respecta filtrele.
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
            if (limit <= number && s.getDuration() != 0 && found) {
                filteredShows.add(s.getTitle());
                limit++;
            }
        }
        return filteredShows;
    }
}
