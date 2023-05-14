
public class SQLQueryTemplateParse {

    public static final String ADD_STAR_PAGE_ = "select add_star(?, ?) as star_id";
    public static final String ADD_MOVIE_PAGE_ = "call add_movie(?, ?, ?, ?, ?, ?)";

    public static final String INSERT_TO_GENRES_ = "select add_genre(?) as genre_id;";

    public static final String LOAD_MOVIE_DATA_ = "load data infile \"cs122b-project1/movies.csv\" into table movies" +
            "FIELDS TERMINATED BY ','" +
            "LINES TERMINATED BY '\n';";

}
