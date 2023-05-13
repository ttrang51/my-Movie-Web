public class Movie {

    private final String fid;

    private final String title;

    private final int year;

    private final String category;
    private final String director;

    public Movie(String fid, String title, int year, String category, String director) {
        this.fid = fid;
        this.title = title;
        this.year = year;
        this.category = category;
        this.director = director;


    }

    public String getFid() {
        return fid;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getCategory() {
        return category;
    }
    public String getDirector() {
        return director;
    }


    public String toString() {

        return "Title:" + getTitle() + ", " +
                "Year:" + getYear() + ", " +
                "ID:" + getFid() + ", "+
                "Category:" + getCategory() + ", "+
                "Director:" + getDirector() + ".";
    }

    public boolean compareTo(Movie otherMovie) {
        if (this.fid == otherMovie.getFid() ) {
            // add other attr checks
            return true;
        }

        return false;

    }
}
