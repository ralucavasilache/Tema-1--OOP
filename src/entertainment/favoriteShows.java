package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;

public class favoriteShows {
    private final List<Show> shows;
    private final int id;
    private final int number;
    private final List<List<String>> filters;
    private final String sortType;
    private final List<User> users;

    public favoriteShows(List<Show> shows, int id, int number, List<List<String>> filters, String sortType, List<User> users) {
        this.shows = new ArrayList<>();
        this.shows.addAll(shows);
        this.id = id;
        this.number = number;
        this.filters = filters;
        this.sortType = sortType;
        this.users = users;
    }

    public JSONObject execute(Writer fileWriter) throws IOException {
        addToFavorite();
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
                if (s1.getFavorite() != s2.getFavorite()) {
                    return Integer.compare(s1.getFavorite(), s2.getFavorite());
                } else {
                    return 0;
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
            if (limit <= number && s.getFavorite() != 0 && found == true) {
                filteredShows.add(s.getTitle());
                limit++;
            }
        }
        return filteredShows;
    }
    void addToFavorite() {
        for(User u : users) {
            for (String movie : u.getFavoriteMovies()) {
                Show s = searchShow(movie);
                if(s != null) {
                    s.setFavorite(1);
                }
            }
        }
    }
    public Show searchShow(String title) {
        for(Show s : shows) {
            if(s.getTitle().equals(title)) {
                return s;
            }

        }
        return null;
    }
}
