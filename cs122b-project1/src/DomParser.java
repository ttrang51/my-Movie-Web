import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.io.IOException;
import java.util.*;
import java.sql.*;

public class DomParser {

    private static Connection connection;

    // Array list of movies
    ArrayList<Movie> movies = new ArrayList<>();
    // HashMap movie to list of cats -- movieId : list(genreId)
    HashMap movie_To_Genres = new HashMap<String, ArrayList<String>>();

    // HashMap genre to its id -- genre_name : genreId (INT)
    HashMap genres = new HashMap<String, Integer>();

    // HashMap stars to their birth_year -- star_name : list(birth_year)
    // List of birth year in case same star name with different birth year
    HashMap stars = new HashMap<String, ArrayList<Integer>>();

    int test_id = 0;

    int dup_movie = 0;
    int dup_actor = 0;
    int null_movie = 0;     // null movieId or title
    int movies_add = 0;
    int actors_add = 0;
    int genres_add = 0;

    Document dom_movie;
    Document dom_actor;
    Document dom_cast;

    public void runExample() {

        // parse the main xml file and get the dom movie object
        parseMainXmlFile();

        // get each employee element and create a Employee object
        parseMainDocument();

        parseActorXML();

        parseActorDocument();


        // iterate through the list and print the data
        //printData();
        System.out.println("dup movie: " + dup_movie);
        System.out.println("null movie: " + null_movie);
        System.out.println("genres:  " + genres);
        System.out.println("movie added:  " + movies_add);
        //printData();

        System.out.println("dup star: " + dup_actor);
        System.out.println("stars: " + stars);
    }

    private void parseMainXmlFile() {
        // get the factory
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {

            // using factory get an instance of document builder
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            // parse using builder to get DOM representation of the XML file
            dom_movie = documentBuilder.parse("stanford-movies/mains243.xml");

        } catch (ParserConfigurationException | SAXException | IOException error) {
            error.printStackTrace();
        }
    }

    private void parseMainDocument(){
        // get the document root Element
        Element documentElement = dom_movie.getDocumentElement();

        // Using HashSet for better search
        Set<String> movieTitles = new HashSet<String>();
        Set<String> movieIds = new HashSet<String>();

        // get a nodelist of movie Elements, parse each into movie object
        NodeList nodeList = documentElement.getElementsByTagName("film");
        for (int i = 0; i < nodeList.getLength(); i++) {
            // get the movie element
            Element element = (Element) nodeList.item(i);
            // get the movie object
            Movie new_movie = parseMovie(element);
            // check for duplicated or invalid here
            if (new_movie.getFid() == null || new_movie.getTitle() == null || new_movie.getCategory().isEmpty())
                null_movie++;
            else if (movieTitles.contains(new_movie.getTitle()) && movieIds.contains(new_movie.getFid()))
                dup_movie++;
            else {      // brand new movie
                movieTitles.add(new_movie.getTitle());
                movieIds.add(new_movie.getFid());
                movies.add(new_movie);
                movies_add++;
            }
            //System.out.println(new_movie.toString());
        }
    }

    /**
     * It takes an employee Element, reads the values in, creates
     * an Employee object for return
     */
    private Movie parseMovie(Element element) {
        // for each <movie> element get text or int values of
        // fid ,year, title, dir, and cats
        String fid = getTextValue(element, "fid");
        int year = getIntValue(element, "year");
        String title = getTextValue(element, "t");
        String dir = getTextValue(element,"dirn");

        // Store set of genres to put in this movie
        HashSet<Integer> genreIds = new HashSet<Integer>();

        PreparedStatement statement = null;

        try {
            // A movie might have multiple <cat*>
            NodeList cats = element.getElementsByTagName("cat");

            for (int i = 0; i < cats.getLength(); i++) {
                if (cats.item(i).getFirstChild() != null) {

                    String cat_name = cats.item(i).getFirstChild().getNodeValue();
                    //System.out.println("cat name: " + cat_name);

                    if (!genres.containsKey(cat_name)){     // new genre is not in genres list yet
                        // Add new genre to genres table
                        /*
                        String query = SQLQueryTemplate.INSERT_TO_GENRES_;
                        statement = connection.prepareStatement(query);
                        statement.setString(1, cat_name);
                        System.out.println("Statement before execution: " + statement);
                        ResultSet rs = statement.executeQuery();
                        while(rs.next()){
                            String cat_id = rs.getString("genre_id");
                            genres.put(cat_name, cat_id);
                            genreIds.add(Integer.parseInt(cat_id));
                        }*/
                        test_id++;
                        genres_add++;
                        genres.put(cat_name, test_id);
                        genreIds.add(test_id);
                        //rs.close();
                    }
                    else {      // if genre is already in genres list. add this genre id to movie
                        genreIds.add((Integer.parseInt(genres.get(cat_name).toString())));
                    }
                    //statement.close();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        //System.out.println("genreIds: " + genreIds);

        // create a new Movie with the value read from the xml nodes
        return new Movie(fid,  title,  year,  genreIds,  dir);
    }

    private void parseActorXML() {
        // get the factory
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {

            // using factory get an instance of document builder
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            // parse using builder to get DOM representation of the XML file
            dom_actor = documentBuilder.parse("stanford-movies/actors63.xml");

        } catch (ParserConfigurationException | SAXException | IOException error) {
            error.printStackTrace();
        }
    }

    private void parseActorDocument() {
        // get the document root Element
        Element documentElement = dom_actor.getDocumentElement();

        // get a nodelist of employee Elements, parse each into Employee object
        NodeList nodeList = documentElement.getElementsByTagName("actor");
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                // get the employee element
                Element actor = (Element) nodeList.item(i);
                //parse and add entries into map
                parseActor(actor);
            }
        }
    }

    private void parseActor(Element element) {
        String star_name = getTextValue(element, "stagename");
        int new_birth_year = getIntValue(element, "dob");
        System.out.println("star name: " + star_name + " byear: " + new_birth_year);
        int duplicated = 0;     // check if a star is duplicated -- no add in that case

        ArrayList<Integer> append_by = new ArrayList<Integer>();
        append_by.add(new_birth_year);

        if (stars.containsKey(star_name)) {     // if a star already in stars. check birth year
            ArrayList<Integer> curr_by = (ArrayList<Integer>) stars.get(star_name);
            if (curr_by.contains(new_birth_year)) {
                dup_actor++;
                duplicated = 1;
            }
            else {    // new birth year of the same star
                curr_by.add(new_birth_year);
                stars.put(star_name, curr_by);
            }
        }
        else {      // add brand new star into stars
            stars.put(star_name, append_by);
        }

        // Insert new star into SQL

    }





    /**
     * It takes an XML element and the tag name, look for the tag and get
     * the text content
     * i.e for <Employee><Name>John</Name></Employee> xml snippet if
     * the Element points to employee node and tagName is name it will return John
     */
    private String getTextValue(Element element, String tagName) {
        String textVal = null;
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            // here we expect only one <Name> would present in the <Employee>
            if (nodeList.item(0).getFirstChild() != null) {
                textVal = nodeList.item(0).getFirstChild().getNodeValue();
            }

        }
        return textVal;
    }

    /**
     * Calls getTextValue and returns a int value
     */
    private int getIntValue(Element ele, String tagName) {
        // in production application you would catch the exception
        int return_int = 0;
        try {
            return_int = Integer.parseInt(getTextValue(ele, tagName));
        } catch(NumberFormatException e){
            return -1;
        }
        return return_int;
    }

    /**
     * Iterate through the list and print the
     * content to console
     */
    private void printData() {

        System.out.println("Total parsed " + movies.size() + " movies");

        for (Movie movie : movies) {
            System.out.println("\t" + movie.toString());
        }
    }

    public static void main(String[] args) {
        // create an instance
        DomParser domParser = new DomParser();
/*
        try {

            // Incorporate mySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

            // Connect to the test database
            connection = DriverManager.getConnection("jdbc:" + mySQLInfo.dbtype + ":///" + mySQLInfo.dbname + "?autoReconnect=true&useSSL=false",
                    mySQLInfo.username, mySQLInfo.password);

            if (connection != null) {
                System.out.println("Connection established!!");
                System.out.println();
            }
            else {
                System.out.println("No connection");
            }

            // call run example
            domParser.runExample();
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        // call run example
        domParser.runExample();

    }

}
