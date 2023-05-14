import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opencsv.CSVWriter;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletConfig;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class DomParser {

    List<Movie> movies = new ArrayList<>();

    List<String> genres = new ArrayList<>();
    Document dom;

    private DataSource dataSource;
    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void runExample() {

        // parse the xml file and get the dom object
        parseXmlFile();

        // get each employee element and create a Employee object
        parseDocument();

        // iterate through the list and print the data
        printData();

        createMovieCSV();

        //insertData();

    }

    private void parseXmlFile() {
        // get the factory
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {

            // using factory get an instance of document builder
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            // parse using builder to get DOM representation of the XML file
            dom = documentBuilder.parse("cs122b-project1/stanford-movies/mains243.xml");

        } catch (ParserConfigurationException | SAXException | IOException error) {
            error.printStackTrace();
        }
    }

    private void parseDocument() {
        // get the document root Element
        Element documentElement = dom.getDocumentElement();

        // get a nodelist of employee Elements, parse each into Employee object
        NodeList nodeList = documentElement.getElementsByTagName("film");
        for (int i = 0; i < nodeList.getLength(); i++) {

            // get the employee element
            Element element = (Element) nodeList.item(i);

            // get the Employee object
            Movie m = parseMovie(element);

            // add it to list
            movies.add(m);
        }
    }

    /**
     * It takes an employee Element, reads the values in, creates
     * an Employee object for return
     */
    private Movie parseMovie(Element element) {

        // for each <movie> element get text or int values of
        // fid ,year, title, dir, and prod
        String fid = getTextValue(element, "fid");
        int year = getIntValue(element, "year");
        String title = getTextValue(element, "t");

        // Get first director from directors List Element
        //Element dir_element = (Element) element.getElementsByTagName("dirs").item(0);
        String dir = getTextValue(element,"dirn");

        //Element cat_element = (Element) element.getElementsByTagName("cats").item(0);
        String cat = getTextValue(element,"cat");

        // Check if category is already seen
        if (!genres.contains(cat)) {
            genres.add(cat);
        }

        System.out.println(fid + ": " + title);


        // create a new Movie with the value read from the xml nodes
        return new Movie(fid,  title,  year,  cat,  dir);
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
            return return_int;
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


    private void createMovieCSV() {
            // first create file object for file placed at location
        // specified by filepath
        File file = new File("movies.csv");
    
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);
    
            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);
    
            // create a List which contains String array
            List<String[]> movie_data = new ArrayList<String[]>();

            int i = 0;
            for (Movie m : movies) {
                movie_data.add(m.getCSVFormat());
                
                if (i == 5) {
                    break;
                }

                i++;
            }
            writer.writeAll(movie_data);
    
            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            
            e.printStackTrace();
        }
    }


    private void insertData() {

        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource

            // Construct a query with parameter represented by "?"
            String query = SQLQueryTemplate.LOAD_MOVIE_DATA_;

            PreparedStatement statement = conn.prepareStatement(query);

            // Perform the query
            ResultSet rs = statement.executeQuery();
            



        } catch (Exception e) {
            System.out.println("Error inserting data:" + e);
            return;
        }

    }

    public static void main(String[] args) {
        // create an instance
        DomParser domParser = new DomParser();

        // call run example
        domParser.runExample();
    }

}
