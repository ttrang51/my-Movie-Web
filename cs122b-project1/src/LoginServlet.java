import com.google.gson.JsonObject;

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
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    // Create a dataSource which registered in web.
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);

        JsonObject responseJsonObject = new JsonObject();

        // Verify reCAPTCHA
        try {
            RecaptchaVerifyUtils.verify(gRecaptchaResponse);
        } catch (Exception e) {
            
            responseJsonObject.addProperty("status", "fail");
            // Log to localhost log
            request.getServletContext().log("reCAPTCHA verification failed");
            String e_message = e.getMessage();
            responseJsonObject.addProperty("message", e_message);

            out.write(responseJsonObject.toString());

            // Set response status to 200
            response.setStatus(200);

            out.close();
            return;
        }


        String username = request.getParameter("username");
        String password = request.getParameter("password");

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */

        response.setContentType("application/json"); // Response mime type


        // Demo
        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource

            // Construct a query with parameter represented by "?"
            String query = "select c.email, c.password\n" +
                    "from customers as c\n" +
                    "where c.email = " + "'" + username + "'";

            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            int row_count = 0;

            // Iterate through each row of rs
            while (rs.next()) {

                String userUserName = rs.getString("email");
                String userPassword = rs.getString("password");

                // Create a JsonObject based on the data we retrieve from rs
                //JsonArray jsonArray = new JsonArray();

                if (username.equals(userUserName)) {
                    row_count += 1;
                    // Login success:
                    if (password.equals(userPassword)) {
                        
                        request.getSession().setAttribute("user", new User(username));

                        responseJsonObject.addProperty("status", "success");
                        responseJsonObject.addProperty("message", "success");
                    } 
                    // Login fail:
                    else {
                        responseJsonObject.addProperty("status", "fail");
                        // Log to localhost log
                        request.getServletContext().log("Login failed");
                        responseJsonObject.addProperty("message", "Incorrect Password");
                    }

                }

            }
            rs.close();
            statement.close();

            if (row_count == 0) {
                JsonObject jsonObject = new JsonObject();
                //jsonObject.addProperty("errorMessage", "Error: Username or Password Incorrect");

                request.getServletContext().log("Login failed");
                // Login fail due to invalid username
                jsonObject.addProperty("status", "fail");
                jsonObject.addProperty("message", "User " + username + " doesn't exist");

                out.write(jsonObject.toString());

                // Set response status to 200
                response.setStatus(200);
            } else {
                // Write JSON string to output
                out.write(responseJsonObject.toString());
                // Set response status to 200 (OK)
                response.setStatus(200);
            }

        } catch (Exception e) {
            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Log error to localhost log
            request.getServletContext().log("Error:", e);
            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }

    }
}
