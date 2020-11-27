package main;

import entities.Actor;
import entities.Movie;
import entities.Show;
import entities.User;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import fileio.Writer;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import org.json.simple.JSONArray;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

        List<ActorInputData> actors = input.getActors();
        List<UserInputData> users = input.getUsers();
        List<ActionInputData> commands = input.getCommands();
        List<MovieInputData> movies = input.getMovies();
        List<SerialInputData> serials = input.getSerials();
        System.out.println(filePath1);

        List<Movie> myMovies = new ArrayList<>();
        for (MovieInputData  m : movies) {
            Movie newMovie = new Movie(m.getTitle(), m.getCast(), m.getGenres(), m.getYear(),
                                        m.getDuration());
            myMovies.add(newMovie);
        }
        List<Show> myShows = new ArrayList<>();
        for (SerialInputData  s : serials) {
            Show newShow = new Show(s.getTitle(), s.getCast(), s.getGenres(), s.getNumberSeason(),
                                    s.getSeasons(), s.getYear());
            myShows.add(newShow);
        }
        List<Actor> myActors = new ArrayList<>();
        for (ActorInputData  a : actors) {
            Actor newActor = new Actor(a.getName(), a.getCareerDescription(), a.getFilmography(),
                                        a.getAwards());
            myActors.add(newActor);
        }
        List<User> myUsers  = new ArrayList<>();
        for (UserInputData u : users) {
            User newUser = new User(u.getUsername(), u.getSubscriptionType(), u.getHistory(),
                                    u.getFavoriteMovies());
            myUsers.add(newUser);
        }
        ActionInterpreter actionInterpretor = new ActionInterpreter(commands, myMovies, myShows,
                                                    myActors, myUsers, fileWriter, arrayResult);
        actionInterpretor.execute();
    }
}
