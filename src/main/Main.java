package main;

import commands.Command;
import entities.Actor;
import entities.Movie;
import entities.Show;
import entities.User;
import entities.Video;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import fileio.Writer;
import queries.NumberOfRatings;
import queries.ActorAwards;
import queries.AverageActors;
import queries.FavoriteMovies;
import queries.FavoriteShows;
import queries.FilterDescription;
import queries.LongestMovie;
import queries.LongestShow;
import queries.MostViewedMovie;
import queries.MostViewedShow;
import queries.RatingMovie;
import queries.RatingShow;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import recommendation.BestUnseen;
import recommendation.FavoritePremium;
import recommendation.Popular;
import recommendation.SearchPremium;
import recommendation.Standard;
import utils.Utils;

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
    public static List<Video> createVideos(final List<Movie> movies, final List<Show> shows) {
        List<Video> videos = new ArrayList<>();
        videos.addAll(movies);
        videos.addAll(shows);
        return videos;
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
        for (MovieInputData  m : movies) {
            Movie newMovie = new Movie(m.getTitle(), m.getCast(), m.getGenres(), m.getYear(),
                                        m.getDuration());
            myMovies.add(newMovie);
        }
        List<Show> myShows = new ArrayList<Show>();
        for (SerialInputData  s : serials) {
            Show newShow = new Show(s.getTitle(), s.getCast(), s.getGenres(), s.getNumberSeason(),
                                    s.getSeasons(), s.getYear());
            myShows.add(newShow);
        }
        List<Actor> myActors = new ArrayList<Actor>();
        for (ActorInputData  a : actors) {
            Actor newActor = new Actor(a.getName(), a.getCareerDescription(), a.getFilmography(),
                                        a.getAwards());
            myActors.add(newActor);
        }
        List<User> myUsers  = new ArrayList<User>();
        for (UserInputData u : users) {
            User newUser = new User(u.getUsername(), u.getSubscriptionType(), u.getHistory(),
                                    u.getFavoriteMovies(), myMovies, myShows);
            myUsers.add(newUser);
        }

        for (ActionInputData a: commands) {
            if (a.getActionType().equals("recommendation")) {
                User user = Utils.searchUser(myUsers, a.getUsername());
                List<Video> videos = createVideos(myMovies, myShows);
                if (a.getType().equals("standard")) {

                    Standard s = new Standard(videos, a.getActionId(), user);
                    JSONObject out = s.execute(fileWriter);
                    arrayResult.add(out);
                } else if (a.getType().equals("best_unseen")) {
                    BestUnseen b = new BestUnseen(videos, a.getActionId(), user);
                    JSONObject out = b.execute(fileWriter);
                    arrayResult.add(out);
                } else if (a.getType().equals("favorite")) {
                    FavoritePremium f = new FavoritePremium(videos, a.getActionId(),
                                                            myUsers, a.getUsername());
                    JSONObject out = f.execute(fileWriter);
                    arrayResult.add(out);
                } else if (a.getType().equals("search")) {
                    String filtre = a.getGenre();
                    SearchPremium f = new SearchPremium(videos, a.getActionId(),
                                                        user, filtre);
                    JSONObject out = f.execute(fileWriter);
                    arrayResult.add(out);
                } else if (a.getType().equals("popular")) {
                    Popular p = new Popular(videos, a.getActionId(),
                                            myUsers, user);
                    JSONObject out = p.execute(fileWriter);
                    arrayResult.add(out);
                }
            } else if (a.getActionType().equals("command")) {
                User user = Utils.searchUser(myUsers, a.getUsername());
                Command command = new Command(a.getType(), user,a.getTitle(), a.getGrade(),
                        a.getSeasonNumber(), a.getActionId(), myMovies, myShows, fileWriter);
                JSONObject out = command.execute();
                arrayResult.add(out);
            } else if (a.getActionType().equals("query")) {
                if (a.getObjectType().equals("actors")) {
                    if (a.getCriteria().equals("average"))  {
                        AverageActors q = new AverageActors(myActors, a.getObjectType(),
                                                            myMovies, myShows, a.getNumber(),
                                                            a.getSortType(), a.getActionId());
                        JSONObject out = q.execute(fileWriter);
                        arrayResult.add(out);
                    } else if (a.getCriteria().equals("filter_description")) {
                        List<String> keywords = a.getFilters().get(2);
                        FilterDescription f = new FilterDescription(myActors, keywords, a.getSortType(),
                                                                    a.getActionId());
                        JSONObject out = f.execute(fileWriter);
                        arrayResult.add(out);
                    } else if (a.getCriteria().equals("awards")) {
                        List<String> awards = a.getFilters().get(3);
                        ActorAwards m = new ActorAwards(myActors, awards, a.getActionId(),
                                                        a.getSortType());
                        JSONObject out = m.execute(fileWriter);
                        arrayResult.add(out);
                    }
                } else if (a.getObjectType().equals("users")) {
                    NumberOfRatings n = new NumberOfRatings(myUsers, a.getNumber(),
                                                            a.getActionId(), a.getSortType());
                    JSONObject out = n.execute(fileWriter);
                    arrayResult.add(out);
                } else if (a.getObjectType().equals("movies")) {
                    if (a.getCriteria().equals("ratings")) {
                        RatingMovie r = new RatingMovie(myMovies, a.getActionId(), a.getNumber(),
                                                        a.getFilters(), a.getSortType());
                        JSONObject out = r.execute(fileWriter);
                        arrayResult.add(out);
                    } else if (a.getCriteria().equals("longest")) {
                        LongestMovie l = new LongestMovie(myMovies, a.getActionId(), a.getNumber(),
                                                            a.getFilters(), a.getSortType());
                        JSONObject out = l.execute(fileWriter);
                        arrayResult.add(out);
                    } else if (a.getCriteria().equals("most_viewed")) {
                        MostViewedMovie m = new MostViewedMovie(myMovies, a.getActionId(), a.getNumber(),
                                                                a.getFilters(), a.getSortType(), myUsers);
                        JSONObject out = m.execute(fileWriter);
                        arrayResult.add(out);
                    } else if (a.getCriteria().equals("favorite")) {
                        FavoriteMovies m = new FavoriteMovies(myMovies, a.getActionId(), a.getNumber(),
                                                                a.getFilters(), a.getSortType(), myUsers);
                        JSONObject out = m.execute(fileWriter);
                        arrayResult.add(out);
                    }
                } else if (a.getObjectType().equals("shows")) {
                    if (a.getCriteria().equals("ratings")) {
                        RatingShow r = new RatingShow(myShows, a.getActionId(), a.getNumber(),
                                                        a.getFilters(), a.getSortType());
                        JSONObject out = r.execute(fileWriter);
                        arrayResult.add(out);
                    } else if (a.getCriteria().equals("longest")) {
                        LongestShow l = new LongestShow(myShows, a.getActionId(), a.getNumber(),
                                                        a.getFilters(), a.getSortType());
                        JSONObject out = l.execute(fileWriter);
                        arrayResult.add(out);
                    } else if (a.getCriteria().equals("most_viewed")) {
                        MostViewedShow m = new MostViewedShow(myShows, a.getActionId(), a.getNumber(),
                                                                a.getFilters(), a.getSortType(), myUsers);
                        JSONObject out = m.execute(fileWriter);
                        arrayResult.add(out);
                    } else if (a.getCriteria().equals("favorite")) {
                        FavoriteShows m = new FavoriteShows(myShows, a.getActionId(), a.getNumber(),
                                                            a.getFilters(), a.getSortType(), myUsers);
                        JSONObject out = m.execute(fileWriter);
                        arrayResult.add(out);
                    }
                }
            }
        }
        fileWriter.closeJSON(arrayResult);
    }
}
