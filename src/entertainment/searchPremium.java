package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class searchPremium {
    private final List<Show> shows;
    private final List<Movie> movies;
    private final int id;
    private final List<User> users;
    private final String username;
    private List<Video> videos;
    private final String filtre;
    private List<Video> filtredVideos = new ArrayList<Video>();

    public searchPremium(List<Show> shows, List<Movie> movies, int id, List<User> users, String username, String filtre) {
        this.shows = shows;
        this.movies = movies;
        this.id = id;
        this.users = users;
        this.username = username;
        this.filtre = filtre;
        videos = new ArrayList<Video>();
        videos.addAll(movies);
        videos.addAll(shows);
    }
    public JSONObject execute(Writer fileWriter) throws IOException {
        filtrebyGenre(filtre);

        User user = searchUser(username);

        if (!user.getSubscriptionType().equals("BASIC") && filtredVideos.size() != 0) {
            ascSort();
            List<String> videostoprint = new ArrayList<String>();
            for(Video v  : filtredVideos) {
                videostoprint.add(v.getTitle());
                System.out.println("**" + v.getTitle());
            }
            System.out.println("**" );

            return fileWriter.writeFile(id, null, "SearchRecommendation result: " + videostoprint);
        } else {
            return fileWriter.writeFile(id, null, "SearchRecommendation cannot be applied!");

        }
            //return fileWriter.writeFile(id, null, "FavoriteRecommendation  cannot be applied");
    }
    private User searchUser(String username) {
        for(User u : users) {
            if(u.getUsername().equals(username)) {
                return  u;
            }
        }
        return null;
    }
    public Video searchVideo(String title) {
        for(Video v : videos) {
            if(v.getTitle().equals(title)) {
                return v;
            }
        }
        return null;
    }
    private void filtrebyGenre (String genre) {

        User user = searchUser(username);
        for(Video v : videos) {
            if(!user.getHistory().containsKey(v.getTitle()) && v.getGenres().contains(genre)) {
                filtredVideos.add(v);
            }
        }
    }
    private void ascSort() {
        Comparator<Video> comparator = new Comparator<Video>() {
            @Override
            public int compare(final Video v1, final Video v2) {
                if (v1.calcAvg() != v2.calcAvg()) {
                    return Double.compare(v1.calcAvg(), v2.calcAvg());
                } else {
                    return v1.getTitle().compareTo(v2.getTitle());
                }
            }
        };

        Collections.sort(filtredVideos, comparator);
    }
}
