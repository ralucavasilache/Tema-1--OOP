package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class popular {
        private final List<Show> shows;
        private final List<Movie> movies;
        private final int id;
        private final List<User> users;
        private final String username;
        private List<Video> videos;
        private Map<String, Integer> genres = new HashMap<String, Integer>();
        private String[] genreEnum = {"TV Movie", "Drama", "Fantasy", "Comedy", "Family", "War", "Sci-Fi & Fantasy",
                                        "Crime", "Animation", "Science Fiction", "Action", "Horror", "Mystery", "Western",
                                        "Adventure", "Action & Adventure", "Romance", "Thriller", "Kids", "History"};
        private List<String> genresRanking ;

        public popular(List<Show> shows, List<Movie> movies, int id, List<User> users, String username) {
            this.shows = shows;
            this.movies = movies;
            this.id = id;
            this.users = users;
            this.username = username;
            videos = new ArrayList<Video>();
            videos.addAll(movies);
            videos.addAll(shows);
        }
         public JSONObject execute(Writer fileWriter) throws IOException {
            viewVideo();
            setGenres();
            calcPopularity();
            User user = searchUser(username);

            if (user.getSubscriptionType().equals("BASIC") || findUnseen()== null) {
                return fileWriter.writeFile(id, null, "PopularRecommendation cannot be applied!");
            } else {
                return fileWriter.writeFile(id, null, "PopularRecommendation result: " + findUnseen());
            }
        }


        void viewVideo() {
            for(User u : users) {
                for (Map.Entry<String,Integer> entry : u.getHistory().entrySet()) {
                    Video v = searchVideo(entry.getKey());
                    if(v != null) {
                        v.setViews(entry.getValue());
                    }
                }
            }
        }
        public Video searchVideo(String title) {
            for(Video v : videos) {
                if(v.getTitle().equals(title)) {
                    return v;
                }
            }
            return null;
        }
        private void setGenres() {
            for (String g : genreEnum) {
                genres.put(g, 0);
            }
//            for (Map.Entry<String, Integer> entry : genres.entrySet()) {
//                System.out.println(entry.getKey() + "   " + entry.getValue());
//            }
            for (Video v : videos) {
                //System.out.println(v.getTitle());
                for (String videoGenre : v.getGenres()) {
                   // System.out.println("    "+videoGenre);
                    genres.put(videoGenre, genres.get(videoGenre) + v.getViews());
                }
            }
        }
        private String findUnseen() {
            int i = 0;
            User user = searchUser(username);
            while(i < genresRanking.size()) {
                for(Video v : videos) {
                    if(!user.getHistory().containsKey(v.getTitle()) && v.getGenres().contains(genresRanking.get(i))) {
                        return v.getTitle();
                    }
                }
                i++;
            }
            return null;
        }
    private User searchUser(String username) {
        for(User u : users) {
            if(u.getUsername().equals(username)) {
                return  u;
            }
        }
        return null;
    }
    //jdhh
        private void calcPopularity() {
            genresRanking = genres.entrySet().stream()
                    .sorted(Comparator.comparing(Map.Entry::getValue))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());


//            for (Map.Entry<String, Integer> entry : genres.entrySet()) {
//                System.out.println(entry.getKey() + "   " + entry.getValue());
//            }
//            System.out.println("*---------------*");
//            for(String s : genresRanking) {
//                System.out.println(s);
//            }
            Collections.reverse(genresRanking);

        }
}
