package queries;

import entities.User;
import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class NumberOfRatings {
    /**
     * Lista cu userii din baza de date
     */
    private final List<User> users;
    /**
     * Numarul maxim de useri care trebuie printati
     */
    private final int number;
    /**
     * Id-ul actiunii
     */
    private final int id;
    /**
     * Tipul sortarii
     */
    private final String sortType;

    public NumberOfRatings(final List<User> users, final int number,
                           final int id, final String sortType) {

        this.users = users;
        this.number = number;
        this.id = id;
        this.sortType = sortType;
    }
    /**
     * Executa actiunea number_of_ratings, prin apelul metodelor corespunzatoare
     * @param fileWriter, obiect Writer ce va scrie mesajul rezultat in urma actiunii
     * @return JSONObject
     */
    public JSONObject execute(final Writer fileWriter) throws IOException {
        sortAsc();
        if (sortType.equals("desc")) {
            Collections.reverse(users);
        }
        return fileWriter.writeFile(id, null, "Query result: " + usersToPrint());
    }
    /**
     * Creeaza o lista finala cu numele a maxim "number" useri
     * (cu ratingsNo != 0), care trebuie afisati.
     */
    private List<String> usersToPrint() {
        int limit = 1;

        List<String> sortedUsers = new ArrayList<>();
        for (User u : users) {
            if (u.getRatingsNo() != 0 && limit <= number) {
                sortedUsers.add(u.getUsername());
                limit++;
            }
        }
        return sortedUsers;
    }
    /**
     * Sorteaza userii crescator numarul de rating-uri acordate, apoi dupa nume.
     */
    private void sortAsc() {
        Comparator<User> comparator = (u1, u2) -> {
            if (u1.getRatingsNo() != u2.getRatingsNo()) {
                return u1.getRatingsNo() - u2.getRatingsNo();
            } else {
                return u1.getUsername().compareTo(u2.getUsername());
            }
        };
        users.sort(comparator);
    }
}
