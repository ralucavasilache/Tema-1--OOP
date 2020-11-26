package queries;

import entities.Show;
import entities.User;
import fileio.Writer;
import org.json.simple.JSONObject;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FavoriteShows {
    private final List<Show> shows;
    private final int id;
    private final int number;
    private final List<List<String>> filters;
    private final String sortType;
    private final List<User> users;

    public FavoriteShows(final List<Show> shows, final int id, final int number,
                         final List<List<String>> filters, final String sortType,
                         final List<User> users) {
        this.shows = new ArrayList<>();
        this.shows.addAll(shows);
        this.id = id;
        this.number = number;
        this.filters = filters;
        this.sortType = sortType;
        this.users = users;
    }

    public JSONObject execute(final Writer fileWriter) throws IOException {
        addToFavorite();
        ascsort();
        if (sortType.equals("desc")) {
            Collections.reverse(shows);
        }
        return fileWriter.writeFile(id, null, "Query result: " + filter());
    }

    private void ascsort() {
        Comparator<Show> comparator = (s1, s2) -> {
            if (s1.getFavorite() != s2.getFavorite()) {
                return Integer.compare(s1.getFavorite(), s2.getFavorite());
            } else {
                return 0;
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
            if (limit <= number && s.getFavorite() != 0 && found) {
                filteredShows.add(s.getTitle());
                limit++;
            }
        }
        return filteredShows;
    }
    private void addToFavorite() {
        for (User u : users) {
            for (String show : u.getFavoriteMovies()) {
                Show s = Utils.searchShow(shows, show);
                if (s != null) {
                    s.setFavorite(1);
                }
            }
        }
    }
}
