import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * A servlet that takes input from a html <form> and talks to MySQL moviedbexample,
 * generates output as a html <table>
 */

// Declaring a WebServlet called FormServlet, which maps to url "/form"
@WebServlet(name = "MBrowseServlet", urlPatterns = "/api/movies-browse")
public class MBrowseServlet extends HttpServlet {

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // Use http GET
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json"); // Response mime type

        HttpSession session = request.getSession();

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()){

            // Declare our statement
            Statement statement = conn.createStatement();

            // Retrieve parameter "name" from the http request, which refers to the value of <input name="name"> in index.html
            String byGenre = request.getParameter("genre");
            String byTitle = request.getParameter("prefix");

            String query = "";
            String baseQuery = "select m.title, m.id, m.year, m.director, t.genres, z.stars, z.starId, rat.rating\n" +
                    "from (select movieId, GROUP_CONCAT(g.name) as genres from genres_in_movies as gim, genres as g\n" +
                    "where gim.genreId = g.id group by movieId order by g.name asc) as t,\n" +
                    "(select movieId, group_concat(s.name order by s.name separator ',') as stars,\n" +
                    "group_concat(sim.starId order by s.name separator ',') as starId\n" +
                    "from stars_in_movies as sim, stars as s where sim.starId = s.id group by movieId) as z,\n" +
                    "movies as m, ratings as rat\n" +
                    "where t.movieId = z.movieId and z.movieId = m.Id and m.Id = rat.movieId and ";

            if (!byGenre.isEmpty())
                query = baseQuery + "t.genres like '%" + byGenre + "%'";
            else {
                if (byTitle.equals("*"))
                    query = baseQuery + "m.title REGEXP '^[^A-Za-z0-9]'";
                else
                    query = baseQuery + "m.title like '" + byTitle + "%'";
            }

            session.setAttribute("query", query);

            session.setAttribute("page", "1");

            // Log to localhost log
            request.getServletContext().log("query：" + query);

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {
                String movie_id = rs.getString("id");
                String movie_name = rs.getString("title");
                String movie_year = rs.getString("year");
                String movie_dir= rs.getString("director");
                String movie_gen = rs.getString("genres");

                String movie_star = rs.getString("stars");
                String movie_star_ids = rs.getString("starID");
                String movie_rating= rs.getString("rating");

                // Create a JsonObject based on the data we retrieve from rs
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_name", movie_name);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_dir", movie_dir);
                jsonObject.addProperty("movie_gen", movie_gen);
                jsonObject.addProperty("movie_star", movie_star);
                jsonObject.addProperty("movie_star_ids", movie_star_ids);
                jsonObject.addProperty("movie_rating", movie_rating);

                jsonArray.add(jsonObject);
            }
            // Close all structures
            rs.close();
            statement.close();

            // Log to localhost log
            request.getServletContext().log("getting " + jsonArray.size() + " results");

            // Write JSON string to output
            out.write(jsonArray.toString());
            // Set response status to 200 (OK)
            response.setStatus(200);

        } catch (Exception e) {
            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }
    }
}
