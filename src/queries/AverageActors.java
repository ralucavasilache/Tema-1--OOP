package queries;

import entities.Actor;
import entities.Movie;
import entities.Show;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class AverageActors {
    /**
     * Lista de actori
     */
    private final List<Actor> actors;
    /**
     * Lista de filme
     */
    private final List<Movie> movies;
    /**
     * Lista de seriale
     */
    private final List<Show> shows;
    /**
     * Numarul maxim de actori care trebuie afisati
     */
    private final int number;
    /**
     * Tipul de sortare
     */
    private final String sortType;
    /**
     * Id-ul actiunii
     */
    private final int id;

    public AverageActors(final List<Actor> actors,
                         final List<Movie> movies, final List<Show> shows,
                         final int number, final String sortType, final int id) {
        this.actors = actors;
        this.movies = movies;
        this.shows = shows;
        this.number = number;
        this.sortType = sortType;
        this.id = id;

    }
    /**
     * Executa actiunea average_actors, prin apelul metodelor corespunzatoare
     * @param fileWriter, obiect Writer ce va scrie mesajul rezultat in urma actiunii
     * @return JSONObject
     */
    public JSONObject execute(final Writer fileWriter) throws IOException {
            parsingFilms();
            calcAvg();
            ascSort();
            if (sortType.equals("desc")) {
                Collections.reverse(actors);
            }
            return fileWriter.writeFile(id, null, "Query result: " + actorsToPrint());
    }
    /**
     * Creeaza o lista finala cu numele actorilor care trebuie printati
     */
    private List<String> actorsToPrint() {
        int limit = 1;
        List<String> sortedActors = new ArrayList<>();
        for (Actor a : actors) {
            if (a.getRating() != 0 && limit <= number) {
                sortedActors.add(a.getName());
                limit++;
            }
        }
        return sortedActors;
    }
    /**
     * Stabileste daca titlurile din filmografia fiecarui actor
     * apartin unui film sau serial
     */
    private void parsingFilms() {
        for (Actor a: actors) {
            List<Movie> actorMovies = new ArrayList<>();
            List<Show> actorShows = new ArrayList<>();
            for (String film : a.getFilmography()) {
                boolean found = false;
                for (Movie m : movies) {
                    if (m.getTitle().equals(film)) {
                        actorMovies.add(m);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    for (Show s : shows) {
                        if (s.getTitle().equals(film)) {
                            actorShows.add(s);
                            break;
                        }
                    }
                }
            }
            a.setMovies(actorMovies);
            a.setShows(actorShows);
        }
    }
    /**
     * Calculeaza rating-ul pentru fiecare actor
     */
    private void calcAvg() {
        for (Actor a: actors) {
            a.calculateRating();
        }
    }
    /**
     * Sorteaza actorii crescator dupa rating, apoi dupa nume
     */
    private void ascSort() {
        Comparator<Actor> comparator = (a1, a2) -> {
            if (a1.getRating() != a2.getRating()) {
                return Double.compare(a1.getRating(), a2.getRating());
            } else {
                return a1.getName().compareTo(a2.getName());
            }
        };
        actors.sort(comparator);
    }
}
