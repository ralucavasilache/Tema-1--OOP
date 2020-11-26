package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class standard {
    private final int id;
    private final User user;
    private List<Video> videos;

    public standard(List<Video> videos, int id, User user, String username) {
        this.videos = videos;
        this.id = id;
        this.user = user;
    }
    public JSONObject execute(Writer fileWriter) throws IOException {
        for(Video v : videos) {
            if(!user.getHistory().containsKey(v.getTitle())) {
                return fileWriter.writeFile(id, null, "StandardRecommendation result: " + v.getTitle());
            }
        }
        return fileWriter.writeFile(id, null, "StandardRecommendation cannot be applied!" );
    }
}
