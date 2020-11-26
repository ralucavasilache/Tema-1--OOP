package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class longestShow {
    private final List<Show> shows;
    private final int id;
    private final int number;
    private final List<List<String>> filters;
    private final String sortType;

    public longestShow(List<Show> show, int id, int number, List<List<String>> filters, String sortType) {
        this.shows = new ArrayList<>();
        this.shows.addAll(show);
        this.id = id;
        this.number = number;
        this.filters = filters;
        this.sortType = sortType;
    }

    public JSONObject execute(Writer fileWriter) throws IOException {
        ascsort();
        if (sortType.equals("desc")) {
            Collections.reverse(shows);
        }
        return fileWriter.writeFile(id, null, "Query result: " + filter());
    }

    private void ascsort() {
        Comparator<Show> comparator = new Comparator<Show>() {
            @Override
            public int compare(final Show s1, final Show s2) {
                if (s1.getDuration() != s2.getDuration()) {
                    return Double.compare(s1.getDuration(), s2.getDuration());
                } else {
                    return s1.getTitle().compareTo(s2.getTitle());
                }
            }
        };
        Collections.sort(shows, comparator);
    }

    private List<String> filter() {
        int limit = 1;

        List<String> filteredShows = new ArrayList<String>();
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
                    }
                }
            }
            if (limit <= number && s.getDuration() != 0 && found == true) {
                filteredShows.add(s.getTitle());
                limit++;
            }
        }
        return filteredShows;
    }
}
