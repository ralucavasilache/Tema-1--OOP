package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NumberOfRatings {
    private final List<User> users;
    private final int number;
    private final int id;
    private final String sortType;

    public NumberOfRatings(List<User> users, int number, int id, String sortType) {
        this.users = users;
        this.number = number;
        this.id = id;
        this.sortType = sortType;
    }
    public JSONObject execute(Writer fileWriter) throws IOException {
        sortAsc();
        if(sortType.equals("desc")) {
            Collections.reverse(users);
        }
        return fileWriter.writeFile(id, null, "Query result: " + usersToPrint());
    }
    private List<String> usersToPrint() {
        int limit = 1;

        List<String> sortedUsers = new ArrayList<String>();
        for(User u : users) {
            if(u.getRatingsNo() != 0 && limit <= number) {
                sortedUsers.add(u.getUsername());
                limit++;
            }
        }
        return sortedUsers;
    }
    private void sortAsc() {
        Comparator<User> comparator = new Comparator<User>(){
            @Override
            public int compare(final User u1, final User u2){
                if(u1.getRatingsNo() != u2.getRatingsNo()) {
                    return u1.getRatingsNo() - u2.getRatingsNo();
                } else {
                    return u1.getUsername().compareTo(u2.getUsername());
                }
            }
        };
        Collections.sort(users, comparator);
    }
}
