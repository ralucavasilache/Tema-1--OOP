package entertainment;

import java.util.ArrayList;

public class Show extends Video {

    private final int numberOfSeasons;
    private int duration;
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
    private final ArrayList<Season> seasons;



    public Show(final String title, final ArrayList<String> cast,
                final ArrayList<String> genres,
                final int numberOfSeasons, final ArrayList<Season> seasons,
                final int year) {
        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
        setDuration();
    }

    public void setRating(double rating, int season, String user) {
       if(season <= numberOfSeasons) {
           seasons.get(season-1).addRating(rating, user);
       }

    }
    private void setDuration() {
        for(Season s : seasons) {
            this.duration += s.getDuration();
        }
    }

    public int getDuration() {
        return duration;
    }

    public int getNumberSeason() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }
    public double calcAvg() {
        double avg = 0;
        for(Season s: seasons) {
            avg += s.calcAvg();
        }
        if(avg != 0) {
            avg = avg/numberOfSeasons;
        }
        //System.out.println(getTitle() + "  : " + avg);
        return avg;
    }

    @Override
    public String toString() {
        return "SerialInputData{" + " title= "
                + super.getTitle() + " " + " year= "
                + super.getYear() + " cast {"
                + super.getCast() + " }\n" + " genres {"
                + super.getGenres() + " }\n "
                + " numberSeason= " + numberOfSeasons
                + ", seasons=" + seasons + "\n\n" + '}';
    }

}
