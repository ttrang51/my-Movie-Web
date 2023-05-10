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

    public static final String U_MOVIE_SEARCH_PAGE_ = "select m.title, m.id, m.year, m.director, t.genres, z.stars, z.starId, rat.rating\n" +
            "from (select movieId, GROUP_CONCAT(g.name order by g.name asc) as genres from genres_in_movies as gim, genres as g\n" +
            "where gim.genreId = g.id group by movieId) as t,\n" +
            "(select movieId, group_concat(s.name order by s.name separator ',') as stars,\n" +
            "group_concat(sim.starId order by s.name separator ',') as starId\n" +
            "from stars_in_movies as sim, stars as s where sim.starId = s.id group by movieId) as z,\n" +
            "movies as m, ratings as rat\n" +
            "where t.movieId = z.movieId and z.movieId = m.Id and m.Id = rat.movieId \n" +
            "and m.title like ? and m.director like ? and z.stars like ? ";

    public static final String MOVIE_BROWSE_PAGE_ = "select m.title, m.id, m.year, m.director, t.genres, z.stars, z.starId, rat.rating\n" +
            "from (select movieId, GROUP_CONCAT(g.name order by g.name asc) as genres from genres_in_movies as gim, genres as g\n" +
            "where gim.genreId = g.id group by movieId) as t,\n" +
            "(select movieId, group_concat(s.name order by s.name separator ',') as stars,\n" +
            "group_concat(sim.starId order by s.name separator ',') as starId\n" +
            "from stars_in_movies as sim, stars as s where sim.starId = s.id group by movieId) as z,\n" +
            "movies as m, ratings as rat\n" +
            "where t.movieId = z.movieId and z.movieId = m.Id and m.Id = rat.movieId and ";

    public static final String U_MOVIE_BROWSE_PAGE_ = "select m.title, m.id, m.year, m.director, t.genres, z.stars, z.starId, rat.rating\n" +
            "from (select movieId, GROUP_CONCAT(g.name order by g.name asc) as genres from genres_in_movies as gim, genres as g\n" +
            "where gim.genreId = g.id group by movieId) as t,\n" +
            "(select movieId, group_concat(s.name order by s.name separator ',') as stars,\n" +
            "group_concat(sim.starId order by s.name separator ',') as starId\n" +
            "from stars_in_movies as sim, stars as s where sim.starId = s.id group by movieId) as z,\n" +
            "movies as m, ratings as rat\n" +
            "where t.movieId = z.movieId and z.movieId = m.Id and m.Id = rat.movieId \n" +
            "and t.genres like ?";

    public static final String CHECK_OUT_PAGE_ = "SELECT * \n" +
            "FROM creditcards \n" +
            "where id = ? and firstName = ? and lastName = ? and expiration = ?";

    public static final String CART_PAGE_ = "select m.id, m.title, m.price from movies as m where ";

    public static final String LOGIN_PAGE_ = "select c.email, c.password from customers as c where c.email = ?";

    public static final String TOP_20_MOVIES_LIST_PAGE_ = "with r(movieId, rating) as (select movieId, rating\n" +
            "from ratings order by rating desc limit 20),\n" +
            "g(movieId, genres) as (select movieId, GROUP_CONCAT(g.name) as genres\n" +
            "from genres_in_movies as gim, genres as g\n" +
            "where gim.genreId = g.id\n" +
            "group by movieId),\n" +
            "s(movieId, stars, starId) as(select movieId, substring_index(group_concat(s.name separator ','), ',', 3) as stars,\n" +
            "substring_index(group_concat(sim.starId separator ','), ',', 3) as starId\n" +
            "from stars_in_movies as sim, stars as s\n" +
            "where sim.starId = s.id\n" +
            "group by movieId)\n" +
            "select Id, m.title, m.year, m.director, genres, stars, s.starId, rating\n" +
            "from movies as m, r, g, s\n" +
            "where r.movieId = m.Id and r.movieId = g.movieId and r.movieId = s.movieId\n" +
            "order by rating desc;";


}
