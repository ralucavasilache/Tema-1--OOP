package queries;

import entities.Actor;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FilterDescription {
    /**
     * Lista cu actorii din baza de date
     */
    private  final List<Actor> actors;
    /**
     * Lista de cuvinte dupa care se face filtrarea
     */
    private final List<String> keywords;
    /**
     * Tipul sortarii
     */
    private final String sortType;
    /**
     * Id-ul actiunii
     */
    private final int id;
    /**
     * Lista cu actorii care respecta filtrele
     */
    private final List<String> filteredActors = new ArrayList<>();

    public FilterDescription(final List<Actor> actors, final List<String> keywords,
                             final String sortType, final int id) {

        this.actors = actors;
        this.keywords = keywords;
        this.sortType = sortType;
        this.id = id;
    }
    /**
     * Executa actiunea filter_description, prin apelul metodelor corespunzatoare
     * @param fileWriter, obiect Writer ce va scrie mesajul rezultat in urma actiunii
     * @return JSONObject
     */
    public JSONObject execute(final Writer fileWriter) throws IOException {
        setFilteredActors();
        ascSort();
        if (sortType.equals("desc")) {
            Collections.reverse(filteredActors);
        }
        return fileWriter.writeFile(id, null, "Query result: " + filteredActors);
    }
    /**
     * Adauga in lista filteredActors actorii care respecta filtrul.
     */
    private void setFilteredActors() {
        for (Actor a: actors) {
            String description = a.getCareerDescription().replaceAll("[^A-Za-z0-9]", " ");
            description = description.toLowerCase();
            String[] split = description.split(" ");
            boolean found = true;
            for (String word: keywords) {
                word = word.toLowerCase();
                if (!Arrays.asList(split).contains(word)) {
                    found = false;
                    break;
                }
            }
            if (found) {
                filteredActors.add(a.getName());
            }
        }
    }
    /**
     * Sorteaza actorii crescator nume.
     */
    private void ascSort() {
        Comparator<String> comparator = (a1, a2) -> {
            if (!a1.equals(a2)) {
                return a1.compareTo(a2);
            } else {
                return 0;
            }
        };
        filteredActors.sort(comparator);
    }
}
