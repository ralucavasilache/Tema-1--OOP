package queries;

import entities.Movie;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RatingMovie {
    private final List<Movie> movies;
    private final int id;
    private  final int number;
    private final List<List<String>> filters;
    private final String sortType;

    public RatingMovie(final List<Movie> movie, final int id, final int number,
                       final List<List<String>> filters, final String sortType) {
        this.movies = new ArrayList<>();
        this.movies.addAll(movie);
        this.id = id;
        this.number = number;
        this.filters = filters;
        this.sortType = sortType;
    }
    public JSONObject execute(final Writer fileWriter) throws IOException {
        ascsort();
        if (sortType.equals("desc")) {
            Collections.reverse(movies);
        }
        return fileWriter.writeFile(id, null, "Query result: " + filter());
    }
    private void ascsort() {
        Comparator<Movie> comparator = (m1, m2) -> {
            if (m1.calcAvg() != m2.calcAvg()) {
                return Double.compare(m1.calcAvg(), m2.calcAvg());
            } else {
                return 0;
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
            if (limit <= number && m.calcAvg() != 0 && found) {
                filteredMovies.add(m.getTitle());
                limit++;
            }
        }
        return filteredMovies;
    }
}
