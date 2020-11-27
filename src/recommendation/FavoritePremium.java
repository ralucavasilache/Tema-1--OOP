package recommendation;

import entities.User;
import entities.Video;
import fileio.Writer;
import org.json.simple.JSONObject;
import utils.Utils;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public final class FavoritePremium {
    /**
     * Id-ul actiunii
     */
    private final int id;
    /**
     * Lista cu userii din baza de date
     */
    private final List<User> users;
    /**
     * Numele utilizatorului care face actiunea
     */
    private final String username;
    /**
     * Lista cu videoclipurile din baza de date
     */
    private final List<Video> videos;

    public FavoritePremium(final List<Video> videos, final int id,
                           final List<User> users, final String username) {

        this.id = id;
        this.users = users;
        this.username = username;
        this.videos = videos;
    }
    /**
     * Executa recomandarea favorite, prin apelul metodelor corespunzatoare
     * @param fileWriter, obiect Writer ce va scrie mesajul rezultat in urma actiunii
     * @return JSONObject
     */
    public JSONObject execute(final Writer fileWriter) throws IOException {
        setFavorite();
        descSort();
        User user = Utils.searchUser(users, username);
        assert user != null;
        if (!user.getSubscriptionType().equals("BASIC")) {
            for (Video v : videos) {
                if (!user.getHistory().containsKey(v.getTitle()) && v.getFavorite() != 0) {

                    return fileWriter.writeFile(id, null, "FavoriteRecommendation result: "
                            + v.getTitle());
                }
            }
        }
        return fileWriter.writeFile(id, null, "FavoriteRecommendation cannot be applied!");
    }
    /**
     * Sorteaza video descrescator numarul de aparitii in listele de favorite,
     * apoi dupa aparitia in baza de date.
     */
    private void descSort() {
        Comparator<Video> comparator = (v1, v2) -> {
            if (v1.getFavorite() != v2.getFavorite()) {
                return v2.getFavorite() - v1.getFavorite();
            } else {
                return videos.indexOf(v1) - videos.indexOf(v2);
            }
        };
        videos.sort(comparator);
    }
    /**
     * Actualizeaza campul favorite pentru toate video care apar in listele
     * de favorite.
     */
    private void setFavorite() {
        for (User u : users) {
            if (!u.getUsername().equals(username)) {
                for (String video : u.getFavoriteMovies()) {
                    Video v = Utils.searchVideo(videos, video);
                    if (v != null) {
                        v.setFavorite(1);
                    }
                }
            }
        }
    }
}
