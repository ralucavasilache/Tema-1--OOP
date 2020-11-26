package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;

public class filterDescription {
    private  final List<Actor> actors;
    private final List<String> keywords;
    private final String sortType;
    private final int id;
    private List<String> filteredActors = new ArrayList<String>();

    public filterDescription(List<Actor> actors, List<String> keywords, String sortType, int id) {
        this.actors = actors;
        this.keywords = keywords;
        this.sortType = sortType;
        this.id = id;
    }
    public JSONObject execute(Writer fileWriter) throws IOException {
        setFilteredActors();
        ascSort();
        if(sortType.equals("desc")) {
            Collections.reverse(filteredActors);
        }
        return fileWriter.writeFile(id, null, "Query result: " + filteredActors);
    }
    private void setFilteredActors() {
        for(Actor a: actors) {
            String description = a.getCareerDescription().replaceAll("[^A-Za-z0-9]", " ").toLowerCase();
            String[] split = description.split(" ");
            boolean found = true;
            for(String word: keywords) {
                word = word.toLowerCase();
                if(!Arrays.asList(split).contains(word)) {
                    found = false;
                    break;
                }
            }
            if(found == true) {
                filteredActors.add(a.getName());
            }
        }
    }
    private void ascSort() {
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(final String a1, final String a2) {
                if (!a1.equals(a2)) {
                    return a1.compareTo(a2);
                } else {
                    return 0;
                }
            }
        };
        Collections.sort(filteredActors, comparator);
    }
}
