package edu.uci.ics.fabflixmobile.ui.singlemovie;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.data.model.Movie;
import edu.uci.ics.fabflixmobile.ui.login.LoginActivity;
import edu.uci.ics.fabflixmobile.ui.movielist.MovieListViewAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SingleMovieActivity extends AppCompatActivity{

    private final String host = "10.0.2.2";
    private final String port = "8080";
    private final String domain = "cs122b_project1_api_example_war";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);
        // TODO: this should be retrieved from the backend server
        getMovie();

    }



    private void fillPage(Movie this_movie) {
        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(this_movie);

        System.out.println("Movie: " + this_movie.getName());

        SingleMovieViewAdapter adapter = new SingleMovieViewAdapter(this, movies);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);

//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            Movie movie = movies.get(position);
//            @SuppressLint("DefaultLocale") String message = String.format("Clicked on position: %d, name: %s, %d", position, movie.getName(), movie.getYear());
//            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//        });
    }

    public void getMovie() {
        // use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is GET

        // TODO: get movie parameters from movie list page using Bundles
        // Filler data
//        String title = "She";
//        String director_name = "";
//        String year = "2003";
//        String star_name = "";

        Bundle bundle = getIntent().getExtras();

        String id = bundle.getString("movie_id"); ;//"tt0488539";

        System.out.println("Movie id: " + id + "\n");

        final StringRequest searchRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/single-movie?id=" + id,
                response -> {
                    // TODO: should parse the json response to update Single Movie Page using search parameters
                    //  upon different response value.

                    Movie this_movie = null;
                    boolean loginSuccess = true;
                    JSONArray json_movies = new JSONArray();
                    try {

                        json_movies = new JSONArray(response);

                        if (json_movies.length() >= 1) {
                            //Complete and destroy login activity once successful
                            //finish();

                            for (int i=0; i < json_movies.length(); i++) {
                                JSONObject m = (JSONObject) json_movies.get(i);

                                // Add movie to movie_list
                                this_movie = new Movie(((String) m.get("movie_id")),
                                        ((String) m.get("movie_title")),
                                        (Short.valueOf((String)m.get("movie_year"))),
                                        ((String)m.get("movie_director")),
                                        ((String)m.get("movie_gen")),
                                        ((String)m.get("movie_star")));

                                System.out.println("Movie Added!");
                            }

                            fillPage(this_movie);
                            // initialize the activity(page)/destination
                            //Intent MovieListPage = new Intent(MovieListActivity.this, MovieListActivity.class);
                            // activate the list page.
                            //startActivity(MovieListPage);


                        } else {
                            //String m = (String) json_respon.get("message");
                            //message.setText("No Movies Found");
                        }

                    } catch (JSONException e) {
                        //message.setText("Sever Error");
                        System.out.println("JSON error: " + e);
                    }
                },
                error -> {
                    // error
                    Log.d("login.error: ", error.toString());
                    System.out.println("Login Error");
                });
        // important: queue.add is where the search request is actually sent
        queue.add(searchRequest);

    }
}
