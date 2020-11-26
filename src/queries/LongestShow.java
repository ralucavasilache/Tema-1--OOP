package queries;

import entities.Show;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LongestShow {
    private final List<Show> shows;
    private final int id;
    private final int number;
    private final List<List<String>> filters;
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

    public JSONObject execute(final Writer fileWriter) throws IOException {
        ascsort();
        if (sortType.equals("desc")) {
            Collections.reverse(shows);
        }
        return fileWriter.writeFile(id, null, "Query result: " + filter());
    }

    private void ascsort() {
        Comparator<Show> comparator = (s1, s2) -> {
            if (s1.getDuration() != s2.getDuration()) {
                return Double.compare(s1.getDuration(), s2.getDuration());
            } else {
                return s1.getTitle().compareTo(s2.getTitle());
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
            if (limit <= number && s.getDuration() != 0 && found) {
                filteredShows.add(s.getTitle());
                limit++;
            }
        }
        return filteredShows;
    }
}
