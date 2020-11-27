package action;

import commands.Command;
import entities.Actor;
import entities.Movie;
import entities.Show;
import entities.User;
import entities.Video;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import queries.ActorAwards;
import queries.AverageActors;
import queries.FavoriteMovies;
import queries.FavoriteShows;
import queries.FilterDescription;
import queries.LongestMovie;
import queries.LongestShow;
import queries.MostViewedMovie;
import queries.MostViewedShow;
import queries.NumberOfRatings;
import queries.RatingMovie;
import queries.RatingShow;
import recommendation.BestUnseen;
import recommendation.FavoritePremium;
import recommendation.Popular;
import recommendation.SearchPremium;
import recommendation.Standard;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ActionInterpreter {
    private final List<ActionInputData> commands;
    private final List<Movie> myMovies;
    private final List<Show> myShows;
    private final List<Actor> myActors;
    private final List<User> myUsers;
    private final Writer fileWriter;
    private final JSONArray arrayResult;
    public static final int FILTER_INDEX = 3;

    public ActionInterpreter(final List<ActionInputData> commands, final List<Movie> myMovies,
                             final List<Show> myShows, final List<Actor> myActors,
                             final List<User> myUsers, final Writer fileWriter,
                             final JSONArray arrayResult) {

        this.commands = commands;
        this.myMovies = myMovies;
        this.myShows = myShows;
        this.myActors = myActors;
        this.myUsers = myUsers;
        this.fileWriter = fileWriter;
        this.arrayResult = arrayResult;
    }
    /**
     * Identifica daca actiunea este recommentation, query sau command
     * si apeleaza metodele specifice
     */
    public void execute() throws IOException {

        for (ActionInputData a : commands) {
            if (a.getActionType().equals("recommendation")) {
                recommendation(a);
            } else if (a.getActionType().equals("command")) {
                command(a);
            } else if (a.getActionType().equals("query")) {
                query(a);
            }
        }
        fileWriter.closeJSON(arrayResult);
    }
    /**
     * Trateaza toate cazurile de recommendation, creeaza obiecte pentru fiecare caz
     * si apeleaza metoda execute specifica. Se adauga rezultatul in arrayResult.
     * @param action actiunea de tip recommendation
     */
    private void recommendation(final ActionInputData action) throws IOException {

        User user = Utils.searchUser(myUsers, action.getUsername());
        List<Video> videos = createVideos(myMovies, myShows);

        if (action.getType().equals("standard")) {
            Standard s = new Standard(videos, action.getActionId(), user);
            JSONObject out = s.execute(fileWriter);
            arrayResult.add(out);

        } else if (action.getType().equals("best_unseen")) {
            BestUnseen b = new BestUnseen(videos, action.getActionId(), user);
            JSONObject out = b.execute(fileWriter);
            arrayResult.add(out);

        } else if (action.getType().equals("favorite")) {
            FavoritePremium f = new FavoritePremium(videos, action.getActionId(),
                                                    myUsers, action.getUsername());
            JSONObject out = f.execute(fileWriter);
            arrayResult.add(out);

        } else if (action.getType().equals("search")) {
            String filter = action.getGenre();
            SearchPremium f = new SearchPremium(videos, action.getActionId(), user, filter);
            JSONObject out = f.execute(fileWriter);
            arrayResult.add(out);

        } else if (action.getType().equals("popular")) {
            Popular p = new Popular(videos, action.getActionId(), myUsers, user);
            JSONObject out = p.execute(fileWriter);
            arrayResult.add(out);
        }
    }
    /**
     * Creeaza un obiect de tip Command si apeleaza metoda execute.
     * Se adauga rezultatul in arrayResult.
     * @param action actiunea de tip command
     */
    private void command(final ActionInputData action) throws IOException {

        User user = Utils.searchUser(myUsers, action.getUsername());
        Command command = new Command(action.getType(), user, action.getTitle(), action.getGrade(),
                                        action.getSeasonNumber(), action.getActionId(), myMovies,
                                        myShows, fileWriter);

        JSONObject out = command.execute();
        arrayResult.add(out);
    }
    /**
     * Trateaza toate cazurile de query, creeaza obiecte pentru fiecare caz
     * si apeleaza metoda execute specifica. Se adauga rezultatul in arrayResult.
     * @param action actiunea de tip query
     */
    private void query(final ActionInputData action) throws IOException {

        if (action.getObjectType().equals("actors")) {

            if (action.getCriteria().equals("average"))  {
                AverageActors q = new AverageActors(myActors, myMovies, myShows,
                                                    action.getNumber(), action.getSortType(),
                                                    action.getActionId());
                JSONObject out = q.execute(fileWriter);
                arrayResult.add(out);

            } else if (action.getCriteria().equals("filter_description")) {
                List<String> keywords = action.getFilters().get(2);
                FilterDescription f = new FilterDescription(myActors, keywords,
                                                            action.getSortType(),
                                                            action.getActionId());
                JSONObject out = f.execute(fileWriter);
                arrayResult.add(out);

            } else if (action.getCriteria().equals("awards")) {
                List<String> awards = action.getFilters().get(FILTER_INDEX);
                ActorAwards m = new ActorAwards(myActors, awards, action.getActionId(),
                                                 action.getSortType());
                JSONObject out = m.execute(fileWriter);
                arrayResult.add(out);
            }

        } else if (action.getObjectType().equals("users")) {
            NumberOfRatings n = new NumberOfRatings(myUsers, action.getNumber(),
                                                    action.getActionId(), action.getSortType());
            JSONObject out = n.execute(fileWriter);
            arrayResult.add(out);

        } else if (action.getObjectType().equals("movies")) {

            if (action.getCriteria().equals("ratings")) {
                RatingMovie r = new RatingMovie(myMovies, action.getActionId(), action.getNumber(),
                                                action.getFilters(), action.getSortType());
                JSONObject out = r.execute(fileWriter);
                arrayResult.add(out);

            } else if (action.getCriteria().equals("longest")) {
                LongestMovie l = new LongestMovie(myMovies, action.getActionId(),
                                                    action.getNumber(), action.getFilters(),
                                                    action.getSortType());
                JSONObject out = l.execute(fileWriter);
                arrayResult.add(out);

            } else if (action.getCriteria().equals("most_viewed")) {
                MostViewedMovie m = new MostViewedMovie(myMovies, action.getActionId(),
                                                        action.getNumber(), action.getFilters(),
                        action.getSortType(), myUsers);
                JSONObject out = m.execute(fileWriter);
                arrayResult.add(out);

            } else if (action.getCriteria().equals("favorite")) {
                FavoriteMovies m = new FavoriteMovies(myMovies, action.getActionId(),
                                                        action.getNumber(), action.getFilters(),
                                                        action.getSortType(), myUsers);
                JSONObject out = m.execute(fileWriter);
                arrayResult.add(out);
            }

        } else if (action.getObjectType().equals("shows")) {

            if (action.getCriteria().equals("ratings")) {
                RatingShow r = new RatingShow(myShows, action.getActionId(), action.getNumber(),
                                                action.getFilters(), action.getSortType());
                JSONObject out = r.execute(fileWriter);
                arrayResult.add(out);

            } else if (action.getCriteria().equals("longest")) {
                LongestShow l = new LongestShow(myShows, action.getActionId(), action.getNumber(),
                                                action.getFilters(), action.getSortType());
                JSONObject out = l.execute(fileWriter);
                arrayResult.add(out);

            } else if (action.getCriteria().equals("most_viewed")) {
                MostViewedShow m = new MostViewedShow(myShows, action.getActionId(),
                                                        action.getNumber(), action.getFilters(),
                                                        action.getSortType(), myUsers);
                JSONObject out = m.execute(fileWriter);
                arrayResult.add(out);

            } else if (action.getCriteria().equals("favorite")) {
                FavoriteShows m = new FavoriteShows(myShows, action.getActionId(),
                                                    action.getNumber(), action.getFilters(),
                                                    action.getSortType(), myUsers);
                JSONObject out = m.execute(fileWriter);
                arrayResult.add(out);
            }
        }
    }
    /**
     * Creeaza o lista de videoclipuri
     * @param movies, filmele din baza de date
     * @param shows, serialele din baza de date
     * @return List<Video>, o lista de videoclipuri
     */
    private List<Video> createVideos(final List<Movie> movies, final List<Show> shows) {
        List<Video> videos = new ArrayList<>();
        videos.addAll(movies);
        videos.addAll(shows);
        return videos;
    }
}
