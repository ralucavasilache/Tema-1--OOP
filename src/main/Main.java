package main;

import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import entertainment.*;
import fileio.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation
        List<ActorInputData> actors = input.getActors();
        List<UserInputData> users = input.getUsers();
        List<ActionInputData> commands = input.getCommands();
        List<MovieInputData> movies = input.getMovies();
        List<SerialInputData> serials = input.getSerials();
        System.out.println(filePath1);


        List<Movie> myMovies = new ArrayList<Movie>();
        for(MovieInputData  m : movies) {
            Movie newMovie = new Movie(m.getTitle(), m.getCast(), m.getGenres(), m.getYear(), m.getDuration());
            myMovies.add(newMovie);
        }

        List<Show> myShows = new ArrayList<Show>();
        for(SerialInputData  s : serials) {
            Show newShow = new Show(s.getTitle(), s.getCast(), s.getGenres(), s.getNumberSeason(), s.getSeasons(), s.getYear());
            myShows.add(newShow);
        }

        List<Actor> myActors = new ArrayList<Actor>();
        for(ActorInputData  a : actors) {
            Actor newActor = new Actor(a.getName(), a.getCareerDescription(), a.getFilmography(), a.getAwards());
            myActors.add(newActor);
        }
        List<User> myusers  = new ArrayList<User>();
        for(UserInputData u : users) {
            User newUser = new User(u.getUsername(), u.getSubscriptionType(), u.getHistory(), u.getFavoriteMovies(), myMovies, myShows);
            myusers.add(newUser);
        }

        for(ActionInputData a: commands) {
            if (a.getActionType().equals("recommendation") && a.getType().equals("standard")) {
                standard s = new standard(myShows, myMovies, a.getActionId(), myusers, a.getUsername());
                JSONObject out = s.execute(fileWriter);
                arrayResult.add(out);
            } else if (a.getActionType().equals("command")) {
                Command command = new Command(a.getType(), a.getUsername(),a.getTitle(), a.getGrade(),
                        a.getSeasonNumber(), a.getActionId(), myusers, myMovies, myShows, fileWriter);
                JSONObject out = command.execute();
                arrayResult.add(out);
            } else if (a.getActionType().equals("query") && a.getObjectType().equals("actors") && a.getCriteria().equals("average")) {
                averageActors q = new averageActors(myActors, a.getObjectType(), myMovies, myShows, a.getNumber(), a.getSortType(), a.getActionId());
                JSONObject out = q.execute(fileWriter);
                arrayResult.add(out);
            }else if (a.getActionType().equals("query") && a.getObjectType().equals("users")) {
                NumberofRatings n = new NumberofRatings(myusers, a.getNumber(), a.getActionId(), a.getSortType());
                JSONObject out = n.execute(fileWriter);
                arrayResult.add(out);
            } else if (a.getActionType().equals("query") && a.getObjectType().equals("movies") && a.getCriteria().equals("ratings")) {
                ratingMovie r = new ratingMovie(myMovies, a.getActionId(),a.getNumber(), a.getFilters(), a.getSortType());
                JSONObject out = r.execute(fileWriter);
                arrayResult.add(out);
            }
            else if (a.getActionType().equals("query") && a.getObjectType().equals("shows") && a.getCriteria().equals("ratings")) {
                ratingShow r = new ratingShow(myShows, a.getActionId(),a.getNumber(), a.getFilters(), a.getSortType());
                JSONObject out = r.execute(fileWriter);
                arrayResult.add(out);
            } else if (a.getActionType().equals("query") && a.getObjectType().equals("movies") && a.getCriteria().equals("longest")) {
                longestMovie l = new longestMovie(myMovies, a.getActionId(),a.getNumber(), a.getFilters(), a.getSortType());
                JSONObject out = l.execute(fileWriter);
                arrayResult.add(out);
            } else if (a.getActionType().equals("query") && a.getObjectType().equals("shows") && a.getCriteria().equals("longest")) {
                longestShow l = new longestShow(myShows, a.getActionId(),a.getNumber(), a.getFilters(), a.getSortType());
                JSONObject out = l.execute(fileWriter);
                arrayResult.add(out);
            }
            else if (a.getActionType().equals("query") && a.getObjectType().equals("movies") && a.getCriteria().equals("most_viewed")) {
                mostViewedMovie m = new mostViewedMovie(myMovies, a.getActionId(),a.getNumber(), a.getFilters(), a.getSortType(), myusers);
                JSONObject out = m.execute(fileWriter);
                arrayResult.add(out);
            }
            else if (a.getActionType().equals("query") && a.getObjectType().equals("shows") && a.getCriteria().equals("most_viewed")) {
                mostViewedShow m = new mostViewedShow(myShows, a.getActionId(),a.getNumber(), a.getFilters(), a.getSortType(), myusers);
                JSONObject out = m.execute(fileWriter);
                arrayResult.add(out);
            }
            else if (a.getActionType().equals("query") && a.getObjectType().equals("movies") && a.getCriteria().equals("favorite")) {
                favoriteMovies m = new favoriteMovies(myMovies, a.getActionId(),a.getNumber(), a.getFilters(), a.getSortType(), myusers);
                JSONObject out = m.execute(fileWriter);
                arrayResult.add(out);
            }
            else if (a.getActionType().equals("query") && a.getObjectType().equals("shows") && a.getCriteria().equals("favorite")) {
                favoriteShows m = new favoriteShows(myShows, a.getActionId(),a.getNumber(), a.getFilters(), a.getSortType(), myusers);
                JSONObject out = m.execute(fileWriter);
                arrayResult.add(out);
            }
            else if (a.getActionType().equals("recommendation") && a.getType().equals("best_unseen")) {
                bestUnseen b = new bestUnseen(myShows, myMovies, a.getActionId(), myusers, a.getUsername());
                JSONObject out = b.execute(fileWriter);
                arrayResult.add(out);
            }
            else if (a.getActionType().equals("recommendation") && a.getType().equals("favorite")) {
                favoritePremium f = new favoritePremium(myShows, myMovies, a.getActionId(), myusers, a.getUsername());
                JSONObject out = f.execute(fileWriter);
                arrayResult.add(out);
            }
            else if (a.getActionType().equals("recommendation") && a.getType().equals("search")) {
                String filtre = a.getGenre();
                searchPremium f = new searchPremium(myShows, myMovies, a.getActionId(), myusers, a.getUsername(), filtre);
                JSONObject out = f.execute(fileWriter);
                arrayResult.add(out);
            }
            else if (a.getActionType().equals("query") && a.getObjectType().equals("actors") && a.getCriteria().equals("filter_description")) {
                List<String> keywords = a.getFilters().get(2);
                filterDescription f = new filterDescription(myActors, keywords, a.getSortType(), a.getActionId());
                JSONObject out = f.execute(fileWriter);
                arrayResult.add(out);
            }
            else if (a.getActionType().equals("query") && a.getObjectType().equals("actors") && a.getCriteria().equals("awards")) {
                List<String> awards = a.getFilters().get(3);
                actorAwards m = new actorAwards(myActors, awards, a.getActionId(),a.getSortType() );
                JSONObject out = m.execute(fileWriter);
                arrayResult.add(out);
            }
            else if (a.getActionType().equals("recommendation") && a.getType().equals("popular")) {
                popular p = new popular(myShows, myMovies, a.getActionId(), myusers, a.getUsername() );
                JSONObject out = p.execute(fileWriter);
                arrayResult.add(out);
                //jhd
            }
        }
        fileWriter.closeJSON(arrayResult);
    }
}
