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

public class ActorAwards {
    private final List<Actor> actors;
    private final List<String> awards;
    private final int id;
    private final String sortType;
    private final List<Actor> filteredActors = new ArrayList<Actor>();

    public ActorAwards(final List<Actor> actors, final List<String> awards,
                       final int id, final String sortType) {
        this.actors = actors;
        this.awards = awards;
        this.id = id;
        this.sortType = sortType;
    }
    public JSONObject execute(final Writer fileWriter) throws IOException {
        setFilteredActors();
        ascsort();

        if (sortType.equals("desc")) {
            Collections.reverse(filteredActors);
        }
        return fileWriter.writeFile(id, null, "Query result: " + actorsToPrint());
    }
    private void setFilteredActors() {
        for (Actor a : actors) {
            boolean ok = findAwards(a);
            if (ok) {
                filteredActors.add(a);
            }
        }
    }
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
    private void ascsort() {
        Comparator<Actor> comparator = (a1, a2) -> {
            if (a1.getAwardsNumber() != a2.getAwardsNumber()) {
                return Integer.compare(a1.getAwardsNumber(), a2.getAwardsNumber());
            } else {
                return a1.getName().compareTo(a2.getName());
            }
        };
        Collections.sort(filteredActors, comparator);
    }
    private List<String> actorsToPrint() {
        List<String> actorsToPrint = new ArrayList<String>();
        for (Actor a : filteredActors) {
            actorsToPrint.add(a.getName());
        }
        return actorsToPrint;
    }
}
