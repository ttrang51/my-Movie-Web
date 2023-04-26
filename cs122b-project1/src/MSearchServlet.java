import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
@WebServlet(name = "FormServlet", urlPatterns = "/form")
public class MSearchServlet extends HttpServlet {

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedbexample");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // Use http GET
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Building page head with title
        out.println("<html><head><title>MovieDBExample: Found Records</title></head>");

        // Building page body
        out.println("<body><h1>MovieDBExample: Found Records</h1>");


        try {

            // Create a new connection to database
            Connection dbCon = dataSource.getConnection();

            // Declare a new statement
            Statement statement = dbCon.createStatement();

            // An array to as flags for search conditions
            int[] flag = {0, 0, 0, 0};      // bit: 0 - not presented else presented
                                            // order: title, year, director, star
            String[] con = {"title", "year", "director", "star"};

            // Retrieve parameter "name" from the http request, which refers to the value of <input name="name"> in index.html
            String movie_title = request.getParameter("movie_title");
            String movie_year = request.getParameter("year");
            String director_name = request.getParameter("director_name");
            String star_name = request.getParameter("star_name");

            flag[0] = movie_title.equals("") ? 0 : 1;
            flag[1] = movie_title.equals("") ? 0 : 1;
            flag[2] = movie_title.equals("") ? 0 : 1;
            flag[3] = movie_title.equals("") ? 0 : 1;

            int num = 0;
            for (int i : flag){
                if (i == 1) num++;
            }
            String query = "";
            String baseQuery = "select m.title, m.year, m.director, t.genres, z.stars, z.starId, rat.rating\n" +
                    "from (select movieId, GROUP_CONCAT(g.name) as genres from genres_in_movies as gim, genres as g\n" +
                    "where gim.genreId = g.id group by movieId) as t,\n" +
                    "(select movieId, group_concat(s.name separator ',') as stars,\n" +
                    "group_concat(sim.starId separator ',') as starId\n" +
                    "from stars_in_movies as sim, stars as s where sim.starId = s.id group by movieId) as z,\n" +
                    "movies as m, ratings as rat\n" +
                    "where t.movieId = z.movieId and z.movieId = m.Id and m.Id = rat.movieId and ";
            if (num == 1) { // Single query search
                // Query search by movie title
                if (flag[0] == 1 && flag[1] != 1 && flag[2] != 1 && flag[3] != 1) {
                    query = "select m.title, m.year, m.director, t.genres, z.stars, z.starId, rat.rating\n" +
                            "from (select movieId, GROUP_CONCAT(g.name) as genres from genres_in_movies as gim, genres as g\n" +
                            "where gim.genreId = g.id group by movieId) as t,\n" +
                            "(select movieId, group_concat(s.name separator ',') as stars,\n" +
                            "group_concat(sim.starId separator ',') as starId\n" +
                            "from stars_in_movies as sim, stars as s where sim.starId = s.id group by movieId) as z,\n" +
                            "movies as m, ratings as rat\n" +
                            "where t.movieId = z.movieId and z.movieId = m.Id and m.Id = rat.movieId and m.title like '% " + movie_title + "%'";
                }
                // Query search by movie year
                else if (flag[0] != 1 && flag[1] == 1 && flag[2] != 1 && flag[3] != 1) {
                    query = "select m.title, m.year, m.director, t.genres, z.stars, z.starId, rat.rating\n" +
                            "from (select movieId, GROUP_CONCAT(g.name) as genres from genres_in_movies as gim, genres as g\n" +
                            "where gim.genreId = g.id group by movieId) as t,\n" +
                            "(select movieId, group_concat(s.name separator ',') as stars,\n" +
                            "group_concat(sim.starId separator ',') as starId\n" +
                            "from stars_in_movies as sim, stars as s where sim.starId = s.id group by movieId) as z,\n" +
                            "movies as m, ratings as rat\n" +
                            "where t.movieId = z.movieId and z.movieId = m.Id and m.Id = rat.movieId and m.year = " + movie_year;
                }
                // Query search by director
                else if (flag[0] != 1 && flag[1] != 1 && flag[2] == 1 && flag[3] != 1) {
                    query = "select m.title, m.year, m.director, t.genres, z.stars, z.starId, rat.rating\n" +
                            "from (select movieId, GROUP_CONCAT(g.name) as genres from genres_in_movies as gim, genres as g\n" +
                            "where gim.genreId = g.id group by movieId) as t,\n" +
                            "(select movieId, group_concat(s.name separator ',') as stars,\n" +
                            "group_concat(sim.starId separator ',') as starId\n" +
                            "from stars_in_movies as sim, stars as s where sim.starId = s.id group by movieId) as z,\n" +
                            "movies as m, ratings as rat\n" +
                            "where t.movieId = z.movieId and z.movieId = m.Id and m.Id = rat.movieId and m.director like '%" + director_name + "%'";
                }
                // Query search by star
                else {
                    query = "select m.title, m.year, m.director, t.genres, z.stars, z.starId, rat.rating\n" +
                            "from (select movieId, GROUP_CONCAT(g.name) as genres from genres_in_movies as gim, genres as g\n" +
                            "where gim.genreId = g.id group by movieId) as t,\n" +
                            "(select movieId, group_concat(s.name separator ',') as stars,\n" +
                            "group_concat(sim.starId separator ',') as starId\n" +
                            "from stars_in_movies as sim, stars as s where sim.starId = s.id and s.name like '%tom%' group by movieId) as z,\n" +
                            "movies as m, ratings as rat\n" +
                            "where t.movieId = z.movieId and z.movieId = m.Id and m.Id = rat.movieId\n";
                }
            }
            else {  // Combined query

            }



            // Log to localhost log
            request.getServletContext().log("query：" + query);

            // Perform the query
            ResultSet rs = statement.executeQuery(query);
            // Create a html <table>
            out.println("<table border>");

            // Iterate through each row of rs and create a table row <tr>
            out.println("<tr><td>ID</td><td>Name</td></tr>");
            while (rs.next()) {
                String m_ID = rs.getString("ID");
                String m_Name = rs.getString("name");
                out.println(String.format("<tr><td>%s</td><td>%s</td></tr>", m_ID, m_Name));
            }
            out.println("</table>");


            // Close all structures
            rs.close();
            statement.close();
            dbCon.close();



        } catch (Exception e) {
            /*
             * After you deploy the WAR file through tomcat manager webpage,
             *   there's no console to see the print messages.
             * Tomcat append all the print messages to the file: tomcat_directory/logs/catalina.out
             *
             * To view the last n lines (for example, 100 lines) of messages you can use:
             *   tail -100 catalina.out
             * This can help you debug your program after deploying it on AWS.
             */
            request.getServletContext().log("Error: ", e);

            // Output Error Massage to html
            out.println(String.format("<html><head><title>MovieDBExample: Error</title></head>\n<body><p>SQL error in doGet: %s</p></body></html>", e.getMessage()));
            return;
        }
        out.close();
    }
}
