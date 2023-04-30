import jakarta.servlet.http.PushBuilder;

public class SQLQueryTemplate {
    public static final String EXTENDED_SINGLE_MOVIE_PAGE_ = "select m.title, m.year, m.director, t.genres, z.stars, z.starId, rat.rating\n" +
            "from (select movieId, GROUP_CONCAT(g.name order by g.name asc) as genres from genres_in_movies as gim, genres as g\n" +
            "where gim.genreId = g.id group by movieId) as t,\n" +
            "(select movieId, group_concat(s.name order by s.name separator ',') as stars,\n" +
            "group_concat(sim.starId order by s.name separator ',') as starId\n" +
            "from stars_in_movies as sim, stars as s where sim.starId = s.id group by movieId) as z,\n" +
            "movies as m, ratings as rat\n" +
            "where t.movieId = ? and z.movieId = ? and m.Id = ? and rat.movieId = ?";

    public static final String EXTENDED_SINGLE_STAR_PAGE_ = "select s.name, s.birthYear, a.titles, a.movieId\n" +
            "from  (select starId, group_concat(m.title order by m.year desc, m.title asc separator ',') as titles,\n" +
            "group_concat(sim.movieId order by m.year desc, m.title asc separator ',') as movieId\n" +
            "from stars_in_movies as sim, movies as m where sim.movieId = m.id group by starId) as a, stars as s\n" +
            "where a.starId = ? and s.Id = ?";

    public static final String MOVIE_SEARCH_PAGE_ = "select m.title, m.id, m.year, m.director, t.genres, z.stars, z.starId, rat.rating\n" +
            "from (select movieId, GROUP_CONCAT(g.name order by g.name asc) as genres from genres_in_movies as gim, genres as g\n" +
            "where gim.genreId = g.id group by movieId) as t,\n" +
            "(select movieId, group_concat(s.name order by s.name separator ',') as stars,\n" +
            "group_concat(sim.starId order by s.name separator ',') as starId\n" +
            "from stars_in_movies as sim, stars as s where sim.starId = s.id group by movieId) as z,\n" +
            "movies as m, ratings as rat\n" +
            "where t.movieId = z.movieId and z.movieId = m.Id and m.Id = rat.movieId ";

    public static final String MOVIE_BROWSE_PAGE_ = "select m.title, m.id, m.year, m.director, t.genres, z.stars, z.starId, rat.rating\n" +
            "from (select movieId, GROUP_CONCAT(g.name) as genres from genres_in_movies as gim, genres as g\n" +
            "where gim.genreId = g.id group by movieId order by g.name asc) as t,\n" +
            "(select movieId, group_concat(s.name order by s.name separator ',') as stars,\n" +
            "group_concat(sim.starId order by s.name separator ',') as starId\n" +
            "from stars_in_movies as sim, stars as s where sim.starId = s.id group by movieId) as z,\n" +
            "movies as m, ratings as rat\n" +
            "where t.movieId = z.movieId and z.movieId = m.Id and m.Id = rat.movieId and ";


}
