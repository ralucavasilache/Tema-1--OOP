package recommendation;

import entities.User;
import entities.Video;
import fileio.Writer;
import org.json.simple.JSONObject;
import utils.Utils;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Popular {
    private final int id;
    private final List<User> users;
    private final User user;
    private final List<Video> videos;
    private final Map<String, Integer> genres = new HashMap<>();
    private final String[] genreEnum = {"TV Movie", "Drama", "Fantasy", "Comedy", "Family", "War",
            "Sci-Fi & Fantasy", "Crime", "Animation", "Science Fiction",
            "Action", "Horror", "Mystery", "Western", "Adventure",
            "Action & Adventure", "Romance", "Thriller", "Kids", "History"};
    private List<String> genresRanking;
    public Popular(final List<Video> videos, final int id,
                   final List<User> users, final User user) {
        this.id = id;
        this.users = users;
        this.user = user;
        this.videos = videos;
    }
    public JSONObject execute(final Writer fileWriter) throws IOException {
        viewVideo();
        setGenres();
        sortByPopularity();
        if (user.getSubscriptionType().equals("BASIC") || findUnseen() == null) {
            return fileWriter.writeFile(id, null, "PopularRecommendation cannot be applied!");
        } else {
            return fileWriter.writeFile(id, null, "PopularRecommendation result: " + findUnseen());
        }
    }
    void viewVideo() {
        for (User u : users) {
            for (Map.Entry<String, Integer> entry : u.getHistory().entrySet()) {
                Video v = Utils.searchVideo(videos, entry.getKey());
                if (v != null) {
                    v.setViews(entry.getValue());
                }
            }
        }
    }
    private void setGenres() {
        for (String genre : genreEnum) {
            genres.put(genre, 0);
        }
        for (Video v : videos) {
            for (String videoGenre : v.getGenres()) {
                genres.put(videoGenre, genres.get(videoGenre) + v.getViews());
            }
        }
    }
    private String findUnseen() {
        int i = 0;
        while (i < genresRanking.size()) {
            for (Video v : videos) {
                boolean found = user.getHistory().containsKey(v.getTitle());
                if (!found && v.getGenres().contains(genresRanking.get(i))) {
                    return v.getTitle();
                }
            }
            i++;
        }
        return null;
    }
    private void sortByPopularity() {
        genresRanking = genres.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
            Collections.reverse(genresRanking);
    }
}
