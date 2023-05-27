package edu.uci.ics.fabflixmobile.data.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Movie class that captures movie information for movies retrieved from MovieListActivity
 */
public class Movie {
    private final String name;
    private final String id;
    private final short year;

    private final String director;
    private final String genres;
    private final String stars;

    public Movie(String id, String name, short year, String director, String genres, String stars) {
        this.name = name;
        this.year = year;
        this.director = director;
        this.genres = genres;
        this.stars = stars;
        this.id = id;
    }

    public String getId() {return id;}

    public String getName() {
        return name;
    }

    public short getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public String getGenres() {
        String[] g = genres.split(",");

        if (g.length < 1) {
            return "";
        } else if (g.length == 1 ) {
            return g[0];
        } else if (g.length == 2 ) {
            return g[0] + ", " + g[1];
        } else {
            return g[0] + ", " + g[1] + ", " + g[2];
        }
    }

    public String getAllGenres() {
        String[] g = genres.split(",");

        if (g.length < 1) {
            return "";
        }

        return String.join(", ", g);
    }

    public String getStars() {
        String[] s = stars.split(",");

        if (s.length < 1) {
            return "";
        } else if (s.length == 1 ) {
            return s[0];
        } else if (s.length == 2 ) {
            return s[0] + ", " + s[1];
        } else {
            return s[0] + ", " + s[1] + ", " + s[2];
        }

    }

    public String getAllStars() {
        String[] s = stars.split(",");

        if (s.length < 1) {
            return "";
        }

        return String.join(", ", s);
    }

}