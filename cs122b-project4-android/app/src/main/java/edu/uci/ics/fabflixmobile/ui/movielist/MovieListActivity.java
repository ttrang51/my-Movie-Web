package edu.uci.ics.fabflixmobile.ui.movielist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.data.model.Movie;
import edu.uci.ics.fabflixmobile.databinding.ActivityLoginBinding;
import edu.uci.ics.fabflixmobile.databinding.ActivityMovielistBinding;
import edu.uci.ics.fabflixmobile.ui.login.LoginActivity;
import edu.uci.ics.fabflixmobile.ui.singlemovie.SingleMovieActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity {

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

    private int page_num = 0;

    private int total_results = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);
        // TODO: this should be retrieved from the backend server
        searchMovies();

    }

    // Called after GET is sent back from the server
    private void fillList(ArrayList<Movie> movies) {
        MovieListViewAdapter adapter = new MovieListViewAdapter(this, movies);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Movie movie = movies.get(position);
            @SuppressLint("DefaultLocale") String message = String.format("Redirecting to %s movie page", movie.getName());
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

            //Complete and destroy Movie List activity once successful
            //finish();
            // initialize the activity(page)/destination
            Intent SingleMoviePage = new Intent(MovieListActivity.this, SingleMovieActivity.class);


            Bundle movie_bundle = new Bundle();
            // Pass movie id to the Single Movie page activity
            movie_bundle.putString("movie_id", movie.getId());

            SingleMoviePage.putExtras(movie_bundle);

            // activate the single movie page.
            startActivity(SingleMoviePage);
        });



        final Button prev_button = findViewById(R.id.prev);
        final Button next_button = findViewById(R.id.next);

        TextView page_text = findViewById(R.id.page_message);
        page_text.setText("You are on Page " + (page_num + 1));

        //assign a listener to call a function to handle the user request when clicking a button
        prev_button.setOnClickListener(view -> prevPage());
        next_button.setOnClickListener(view -> nextPage());
        // TODO: add pagination next/prev button functionality
    }

    public ArrayList<Movie> searchMovies() {
        ArrayList<Movie> movie_list = new ArrayList<>();

        // use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is GET

        // TODO: get search parameters from search activity using Bundles

        Bundle bundle = getIntent().getExtras();

        total_results = Integer.parseInt(bundle.getString("num_results"));

        String id = bundle.getString("movie_title"); ;//"tt0488539";


        String title = bundle.getString("movie_name");
        String director_name = bundle.getString("movie_dir");
        String year = bundle.getString("movie_year");
        String star_name = bundle.getString("movie_star");

        final StringRequest searchRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/fulltext-search?ft_movie_title=" + title, //"/api/movies-search?movie_title=" + title + "&director_name=" + director_name + "&year=" + year + "&star_name=" + star_name,
                response -> {
                    // TODO: should parse the json response to update Movie List Page using search parameters
                    //  upon different response value.

                    boolean loginSuccess = true;
                    JSONArray json_movies = new JSONArray();
                    try {

                        json_movies = new JSONArray(response);
                        total_results = json_movies.length();

                        if (json_movies.length() >= 1) {
                            //Complete and destroy login activity once successful
                            //finish();

                            // Offset display results based on current page number
                            int start_i = 0;
                            int stop_i = 0;
                            if (total_results < 10) {
                                start_i = 0;
                                stop_i = total_results;
                            } else if (total_results - (page_num * 10) > 10 ){
                                start_i = (page_num * 10);
                                stop_i = (page_num * 10) + 10;
                            } else { // if total_results - (page_num * 10) <= 10
                                start_i = (page_num * 10);
                                stop_i = (page_num * 10) + (total_results - (page_num * 10));
                            }

                            for (int i = start_i; i < stop_i; i++) {
                                JSONObject m = (JSONObject) json_movies.get(i);

                                // Add movie to movie_list
                                movie_list.add(new Movie(((String) m.get("movie_id")),
                                                ((String) m.get("movie_name")),
                                                (Short.valueOf((String)m.get("movie_year"))),
                                                ((String)m.get("movie_dir")),
                                                ((String)m.get("movie_gen")),
                                                ((String)m.get("movie_star"))));

                            }

                            fillList(movie_list);


                        } else {
                            //String m = (String) json_respon.get("message");
                            //message.setText("No Movies Found");
                        }

                    } catch (JSONException e) {
                        //message.setText("Sever Error");
                    }
                },
                error -> {
                    // error
                    Log.d("login.error", error.toString());
                });
        // important: queue.add is where the search request is actually sent
        queue.add(searchRequest);


        return movie_list;
    }


    public void nextPage() {

        @SuppressLint("DefaultLocale") String message = "";
        if (total_results == 0) {
            System.out.println("You're on the only page!");
            message = String.format("You're on the only page!");
        } else if (Math.floor(total_results / 10.0) == 0) {
            System.out.println("You're on the only page!");
            message = String.format("You're on the only page!");
        } else if (page_num  == Math.floor(total_results / 10.0) - 1) {
            System.out.println("You're on the last page!");
            message = String.format("You're on the last page!");
        } else {
            page_num++;
            searchMovies();
            System.out.println("You are now on page: " + (page_num + 1));
            message = String.format("You are now on page: " + (page_num + 1));
        }

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

    public void prevPage() {

        @SuppressLint("DefaultLocale") String message = "";
        if (page_num  == 0) {
            System.out.println("You're on the First page!");
            message = String.format("You're on the First page!");
        } else {
            page_num--;
            searchMovies();
            System.out.println("You are now on page: " + page_num);
            message = String.format("You are now on page: " + (page_num + 1));
        }

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}

