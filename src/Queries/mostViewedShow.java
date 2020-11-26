package Queries;

import Entities.Show;
import Entities.User;
import fileio.Writer;
import org.json.simple.JSONObject;
import utils.Utils;

import java.io.IOException;
import java.util.*;

public class mostViewedShow {
    private final List<Show> shows;
    private final int id;
    private final int number;
    private final List<List<String>> filters;
    private final String sortType;
    private final List<User> users;

    public mostViewedShow(List<Show> shows, int id, int number, List<List<String>> filters, String sortType, List<User> users) {
        this.shows = new ArrayList<>();
        this.shows.addAll(shows);
        this.id = id;
        this.number = number;
        this.filters = filters;
        this.sortType = sortType;
        this.users = users;
    }

    public JSONObject execute(Writer fileWriter) throws IOException {
        viewShows();
        ascsort();
        if (sortType.equals("desc")) {
            Collections.reverse(shows);
        }
        return fileWriter.writeFile(id, null, "Query result: " + filter());
    }

    private void ascsort() {
        Comparator<Show> comparator = (s1, s2) -> {
            if (s1.getViews() != s2.getViews()) {
                return Integer.compare(s1.getViews(), s2.getViews());
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
            if (limit <= number && s.getViews() != 0 && found == true) {
                filteredShows.add(s.getTitle());
                limit++;
            }
        }
        return filteredShows;
    }
    void viewShows() {
        for(User u : users) {
            for (Map.Entry<String,Integer> entry : u.getHistory().entrySet()) {
                Show s = Utils.searchShow(shows, entry.getKey());
                if(s != null) {
                    s.setViews(entry.getValue());
                }
            }
        }
    }
}
