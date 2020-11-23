package entertainment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Movie extends Video {

    private final int duration;
    private int views = 0;
    private int favorite = 0;

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views += views;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite += favorite;
    }


    private Map<String, Double> rating = new HashMap<String, Double>();



    public Movie(final String title, final ArrayList<String> cast,
                 final ArrayList<String> genres, final int year,
                 final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
    }

    public void setRating(double rating, String user) {
        this.rating.put(user, rating);
    }

    public int getDuration() {
        return duration;
    }
    public Map<String, Double> getRating() {
        return rating;
    }

    public double calcAvg() {
        double avg = 0;
        for (double r : rating.values()) {
            avg += r;

        }
        if(avg != 0) {
            avg = avg/rating.size();
        }
        //System.out.println(getTitle() + "  : " + avg);
        return avg;
    }

    @Override
    public String toString() {
        return "Movie{" + "title= "
                + super.getTitle() + "year= "
                + super.getYear() + "duration= "
                + duration + "cast {"
                + super.getCast() + " }\n"
                + "genres {" + super.getGenres() + " }\n "
                + "ratings {" + rating + " }\n ";
    }
}
