package edu.uci.ics.fabflixmobile.ui.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.databinding.ActivityLoginBinding;
import edu.uci.ics.fabflixmobile.databinding.ActivitySearchBinding;
import edu.uci.ics.fabflixmobile.ui.login.LoginActivity;
import edu.uci.ics.fabflixmobile.ui.movielist.MovieListActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class SearchActivity extends AppCompatActivity{

    private EditText movie_title;
    private EditText movie_dir;
    private EditText movie_year;
    private EditText movie_star;
    private TextView message;

    /*
  In Android, localhost is the address of the device or the emulator.
  To connect to your machine, you need to use the below IP address
 */
    // Local URL
//    private final String host = "10.0.2.2";
//    private final String port = "8080";
//    private final String domain = "cs122b_project1_api_example_war";
//    private final String baseURL = "http://" + host + ":" + port + "/" + domain;

    // AWS Server URL
    private final String host = "ec2-3-144-164-209.us-east-2.compute.amazonaws.com";
    private final String port = "8443";
    private final String domain = "cs122b-project1-api-example";
    private final String baseURL = "https://" + host + ":" + port + "/" + domain;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ActivitySearchBinding binding = ActivitySearchBinding.inflate(getLayoutInflater());
        // upon creation, inflate and initialize the layout
        setContentView(binding.getRoot());

        movie_title = binding.movieTitle;
        movie_dir = binding.movieDirector;
        movie_year = binding.movieYear;
        movie_star = binding.movieStar;
        message = binding.message;
        final Button searchButton = binding.search;

        //assign a listener to call a function to handle the user request when clicking a button
        searchButton.setOnClickListener(view -> search());
    }


    @SuppressLint("SetTextI18n")
    public void search() {
        message.setText("Searching...");
        // use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        // request type is POST
        final StringRequest searchRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/fulltext-search?ft_movie_title=" + movie_title.getText(), //api/movies-search?movie_title=" + movie_title.getText() + "&director_name="+ movie_dir.getText() + "&year="+ movie_year.getText() +  "&star_name=" + movie_star.getText(),
                response -> {
                    // TODO: should parse the json response to redirect to appropriate functions
                    //  upon different response value.

                    boolean loginSuccess = true;
                    JSONArray json_movies = new JSONArray();
                    try {
                        json_movies = new JSONArray(response);


                        if (json_movies.length() >= 1) {
                            Bundle search_bundle = new Bundle();
                            // Pass search parameters to Movie List Page
                            search_bundle.putString("num_results", Integer.toString(json_movies.length()));
                            search_bundle.putString("movie_name", movie_title.getText().toString());
                            search_bundle.putString("movie_year", movie_year.getText().toString());
                            search_bundle.putString("movie_dir", movie_dir.getText().toString());
                            search_bundle.putString("movie_star", movie_star.getText().toString());

                            Intent MovieListPage = new Intent(SearchActivity.this, MovieListActivity.class);

                            MovieListPage.putExtras(search_bundle);

                            startActivity(MovieListPage);
                        } else {
                            message.setText("No Results");
                        }

                    } catch (JSONException e) {
                        message.setText("Sever Error");
                    }
                },
                error -> {
                    // error
                    Log.d("login.error", error.toString());
                });

        // important: queue.add is where the search request is actually sent
        queue.add(searchRequest);
    }

}
