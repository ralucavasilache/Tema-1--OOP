package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NumberofRatings {
    private final List<User> users;
    private final int number;
    private final int id;
    private final String sortType;

    public NumberofRatings(List<User> users, int number, int id, String sortType) {
        this.users = users;
        this.number = number;
        this.id = id;
        this.sortType = sortType;
    }

    public JSONObject execute(Writer fileWriter) throws IOException {

        if(sortType.equals("asc")) {
            sortAsc();
        } else {
            sortDesc();
        }
        int limit = 1;

        List<String> sortedUsers = new ArrayList<String>();
        for(User u : users) {
            if(u.getRatingsNo() != 0 && limit <= number) {
                sortedUsers.add(u.getUsername());
                limit++;
            }
        }
        return fileWriter.writeFile(id, null, "Query result: " + sortedUsers);
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
        for(User u : users) {
           // System.out.println(u.getUsername() + " : " + u.getRatingsNo());
        }
    }
    private void sortDesc() {
        Comparator<User> comparator = new Comparator<User>(){
            @Override
            public int compare(final User u1, final User u2){
                if(u1.getRatingsNo() != u2.getRatingsNo()) {
                    return u2.getRatingsNo() - u1.getRatingsNo();
                } else {
                    return u2.getUsername().compareTo(u1.getUsername());
                }
            }
        };
        Collections.sort(users, comparator);
//        for(User u : users) {
//            // System.out.println(u.getUsername() + " : " + u.getRatingsNo());
//        }
    }
}
