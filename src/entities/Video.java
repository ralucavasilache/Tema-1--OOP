package entities;

import java.util.ArrayList;
/**
 * Informatii generale despre un video
 * Metode specifice
 */
public abstract class Video {
    /**
     * Titlul videoclipului
     */
    private final String title;
    /**
     * Anul aparitiei
     */
    private final int year;
    /**
     * Actorii care au jucat in el
     */
    private final ArrayList<String> cast;
    /**
     * Genurile in care se incadreaza
     */
    private final ArrayList<String> genres;
    /**
     * Numarul de aparitii in listele de favorite
     */
    private int favorite = 0;
    /**
     * Numarul de vizualizari
     */
    private int views = 0;

    public Video(final String title, final int year,
                 final ArrayList<String> cast, final ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
    }

    public final String getTitle() {
        return title;
    }

    public final int getYear() {
        return year;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }

    public final int getFavorite() {
        return favorite;
    }

    public final int getViews() {
        return views;
    }
    /**
     * Actualizeaza numarul de aparitii in listele de favorite
     * @param favorite, noi aparitii in listele de favorite
     */
    public final void setFavorite(final int favorite) {
        this.favorite += favorite;
    }
    /**
     * Actualizeaza numarul de vizualizari
     * @param  views, noi vizualizari primite
     */
    public final void setViews(final int views) {
        this.views += views;
    }
    /**
     * Calculeaza media rating-urilor
     */
    public abstract double calcAvg();
}
