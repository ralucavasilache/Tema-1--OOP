package recommendation;

import entities.User;
import entities.Video;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

public final class Standard {
    /**
     * Id-ul actiunii
     */
    private final int id;
    /**
     * Numele utilizatorului care face actiunea
     */
    private final User user;
    /**
     * Lista de videoclipuri din baza de date
     */
    private final List<Video> videos;

    public Standard(final List<Video> videos, final int id, final User user) {

        this.videos = videos;
        this.id = id;
        this.user = user;
    }
    /**
     * Executa recomandarea standard, prin apelul metodelor corespunzatoare
     * @param fileWriter, obiect Writer ce va scrie mesajul rezultat in urma actiunii
     * @return JSONObject
     */
    public JSONObject execute(final Writer fileWriter) throws IOException {
        for (Video v : videos) {
            if (!user.getHistory().containsKey(v.getTitle())) {
                return fileWriter.writeFile(id, null, "StandardRecommendation result: "
                                            + v.getTitle());
            }
        }
        return fileWriter.writeFile(id, null, "StandardRecommendation cannot be applied!");
    }
}
