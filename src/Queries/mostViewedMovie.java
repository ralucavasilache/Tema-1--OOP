package Queries;

import Entities.Movie;
import Entities.User;
import fileio.Writer;
import org.json.simple.JSONObject;
import utils.Utils;

import java.io.IOException;
import java.util.*;

public class mostViewedMovie {
    private final List<Movie> movies;
    private final int id;
    private final int number;
    private final List<List<String>> filters;
    private final String sortType;
    private final List<User> users;

    public mostViewedMovie(List<Movie> movie, int id, int number, List<List<String>> filters, String sortType, List<User> users) {
        this.movies = new ArrayList<>();
        this.movies.addAll(movie);
        this.id = id;
        this.number = number;
        this.filters = filters;
        this.sortType = sortType;
        this.users = users;
    }

    public JSONObject execute(Writer fileWriter) throws IOException {
        viewMovies();
        ascsort();
        if (sortType.equals("desc")) {
            Collections.reverse(movies);
        }
        return fileWriter.writeFile(id, null, "Query result: " + filter());
    }
    private void ascsort() {
        Comparator<Movie> comparator = (m1, m2) -> {
            if (m1.getViews() != m2.getViews()) {
                return Integer.compare(m1.getViews(), m2.getViews());
            } else {
                return m1.getTitle().compareTo(m2.getTitle());
            }
        };

        Collections.sort(movies, comparator);
    }
    private List<String> filter() {
        int limit = 1;

        List<String> filteredMovies = new ArrayList<String>();
        for (Movie m : movies) {
            boolean found = true;
            if (filters.get(0).get(0) != null) {
                if (!String.valueOf(m.getYear()).equals(filters.get(0).get(0))) {
                    found = false;
                }
            }
            if (filters.get(1).get(0) != null) {
                for (String genre : filters.get(1)) {
                    if (!m.getGenres().contains(genre)) {
                        found = false;
                    }
                }
            }
            if (limit <= number && m.getViews() != 0 && found == true) {
                filteredMovies.add(m.getTitle());
                limit++;
            }
        }
        return filteredMovies;
    }
    private void viewMovies() {
        for(User u : users) {
            for (Map.Entry<String,Integer> entry : u.getHistory().entrySet()) {
                Movie m = Utils.searchMovie(movies, entry.getKey());
                if(m != null) {
                    m.setViews(entry.getValue());
                }
            }
        }
    }
}
