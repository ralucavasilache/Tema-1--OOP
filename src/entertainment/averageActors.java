package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class averageActors {
    private final List<Actor> actors;
    private final List<Movie> movies;
    private final List<Show> shows;
    private List<Movie> actorMovies;
    private List<Show> actorShows;
    private final String objType;
    private final int number;
    private final String sortType;
    private final int id;

    public averageActors(List<Actor> actors, String type, List<Movie> movies, List<Show> shows,
                         int number, String sortType, int id) {
        this.actors = actors;
        this.objType = type;
        this.movies = movies;
        this.shows = shows;
        this.number = number;
        this.sortType = sortType;
        this.id = id;

    }
    public JSONObject execute(Writer fileWriter) throws IOException {
            parsingFilms();
            calcAvg();
            ascSort();
            if(sortType.equals("desc")){
                Collections.reverse(actors);
            }
            return fileWriter.writeFile(id, null, "Query result: " + actorsToPrint());
    }
    private List<String> actorsToPrint() {
        int limit = 1;
        List<String> sortedActors = new ArrayList<String>();
        for(Actor a : actors) {
            if(a.getRating() != 0 && limit <= number) {
                sortedActors.add(a.getName());
                limit++;
            }
        }
        return sortedActors;
    }
    public void parsingFilms() {
        for(Actor a: actors) {
            actorMovies = new ArrayList<>();
            actorShows = new ArrayList<>();
            for(String film : a.getFilmography()) {
                boolean found = false;
                for(Movie m : movies) {
                    if(m.getTitle().equals(film)) {
                        actorMovies.add(m);
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    for(Show s : shows) {
                        if(s.getTitle().equals(film)) {
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
    private void calcAvg() {
        for(Actor a: actors) {
            a.calculateRating();
        }
    }
    private void ascSort() {
        Comparator<Actor> comparator = new Comparator<Actor>(){
            @Override
            public int compare(final Actor a1, final Actor a2){
                if(a1.getRating() != a2.getRating()){
                    return Double.compare( a1.getRating(), a2.getRating());
                }else{
                    return a1.getName().compareTo(a2.getName());
                }
            }
        };
        Collections.sort(actors, comparator);
    }
}
