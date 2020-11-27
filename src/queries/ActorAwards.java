package queries;

import actor.ActorsAwards;
import entities.Actor;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class ActorAwards {
    /**
     * Lista de actori
     */
    private final List<Actor> actors;
    /**
     * Lista de premii
     */
    private final List<String> awards;
    /**
     * Id-ul actiunii
     */
    private final int id;
    /**
     * Tipul de sortare
     */
    private final String sortType;
    /**
     * Lista de actori care respecta filtrele
     */
    private final List<Actor> filteredActors = new ArrayList<>();

    public ActorAwards(final List<Actor> actors, final List<String> awards,
                       final int id, final String sortType) {
        this.actors = actors;
        this.awards = awards;
        this.id = id;
        this.sortType = sortType;
    }
    /**
     * Executa actiunea awards_actors, prin apelul metodelor corespunzatoare
     * @param fileWriter, obiect Writer ce va scrie mesajul rezultat in urma actiunii
     * @return JSONObject
     */
    public JSONObject execute(final Writer fileWriter) throws IOException {
        setFilteredActors();
        ascsort();

        if (sortType.equals("desc")) {
            Collections.reverse(filteredActors);
        }
        return fileWriter.writeFile(id, null, "Query result: " + actorsToPrint());
    }
    /**
     * Adauga in lista filteredActors, actorii care respecta filtrul
     * (au obtinut toate premiile din awards)
     */
    private void setFilteredActors() {
        for (Actor a : actors) {
            boolean ok = findAwards(a);
            if (ok) {
                filteredActors.add(a);
            }
        }
    }
    /**
     * Cauta fiecare premiu din awards in lista de premii a unui actor
     * @param actor, cel pentru care se face cautarea
     * @return true, daca au fost gasite toate premiile
     */
    private boolean findAwards(final Actor actor) {
        for (String award : awards) {
            boolean found = false;
            for (Map.Entry<ActorsAwards, Integer> entry : actor.getAwards().entrySet()) {
                if (entry.getKey().toString().equals(award)) {
                    found = true;
                }
            }
            if (!found) {
                return  false;
            }
        }
        return true;
    }
    /**
     * Sorteaza crescator dupa numarul total de premii,
     * apoi dupa nume, lista filteredActors
     */
    private void ascsort() {
        Comparator<Actor> comparator = (a1, a2) -> {
            if (a1.calcAwardsNumber() != a2.calcAwardsNumber()) {
                return Integer.compare(a1.calcAwardsNumber(), a2.calcAwardsNumber());
            } else {
                return a1.getName().compareTo(a2.getName());
            }
        };
        filteredActors.sort(comparator);
    }
    /**
     * Creeaza o lista finala cu numele actorilor care trebuie printati
     */
    private List<String> actorsToPrint() {
        List<String> actorsToPrint = new ArrayList<>();
        for (Actor a : filteredActors) {
            actorsToPrint.add(a.getName());
        }
        return actorsToPrint;
    }
}
