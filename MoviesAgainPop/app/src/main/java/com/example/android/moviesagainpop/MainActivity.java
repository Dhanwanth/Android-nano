package com.example.android.moviesagainpop;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;


public class MainActivity extends ActionBarActivity {
    String API_KEY="";//API Key to be added here
    ImageAdapter imgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        String[] data = {
                "/tbhdm8UJAb4ViCTsulYFL3lxMCd.jpg && && &&",
                "/uXZYawqUsChGSj54wcuBtEdUJbh.jpg && && &&",
                "/69Cz9VNQZy39fUE2g0Ggth6SBTM.jpg && && &&",
                "/t90Y3G8UGQp0f0DrP60wRu9gfrH.jpg && && &&",
        };
        imgAdapter=new ImageAdapter(this,data);

        gridview.setAdapter(imgAdapter);
        FetchMovies moVies = new FetchMovies();
        moVies.execute("P");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.sortby_popularity) {
            FetchMovies mos = new FetchMovies();
            mos.execute("P");
            return true;
        }
        else if(id==R.id.sortby_Ratings) {
            FetchMovies mos = new FetchMovies();
            mos.execute("R");
            return true;
        }

        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
         //   return true;
        //}

        return super.onOptionsItemSelected(item);
    }
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
private String[] mStringURI;
        public ImageAdapter(Context c,String[] strURI) {
            mContext = c;
            mStringURI=strURI;
        }

        public int getCount() {
            return mStringURI.length;
        }

        public Object getItem(int position) {
            return null;
        }
public void add(String[] s)
{

    mStringURI=s;
}
        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
              //  imageView.setScaleType(ImageView.ScaleType.);
                //imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/"+mStringURI[position].split(" && ")[0]).into(imageView);
            //imageView.setImageResource(mThumbIds[position]);
            final String movieDetails=mStringURI[position];
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent int1=new Intent(mContext,MovieDetails.class);
                    int1.putExtra("MovieDetails",movieDetails);
                    int1.setType("text/plain");
                    startActivity(int1);
                }
            });
            return imageView;
        }

        // references to our images

    }
    public class FetchMovies extends AsyncTask<String,Void,String[]>
    {

        private final String LOG_TAG = FetchMovies.class.getSimpleName();

        private String[] getMoviesList(String MovieListJsonStr, int numDays)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "results";

            final String OWM_ORGNAME = "original_title";
            final String OWM_MIN = "release_date";
            final String OWM_DESCRIPTION = "overview";
            final String OWM_POSTERPATH="poster_path";
            final String OWM_VOTEAVG="vote_average";
            JSONObject MovieJson = new JSONObject(MovieListJsonStr);
            JSONArray resutsArray = MovieJson.getJSONArray(OWM_LIST);




            String[] resultStrs = new String[resutsArray.length()];
            for (int i = 0; i < resutsArray.length(); i++) {


                String description;

                String ImagePath;
                String OrginalMovieName;
                String VoteAverage;
                String ReleaseDate;
                // Get the JSON object representing the movie
                JSONObject movieObject = resutsArray.getJSONObject(i);


                description = movieObject.getString(OWM_DESCRIPTION);
                ImagePath= movieObject.getString(OWM_POSTERPATH);
                OrginalMovieName= movieObject.getString(OWM_ORGNAME);
                VoteAverage=movieObject.getString(OWM_VOTEAVG);
                ReleaseDate=movieObject.getString(OWM_MIN);
                resultStrs[i] = ImagePath + " && " + description + " && " + OrginalMovieName + " && " +VoteAverage + " && " + ReleaseDate;
            }


            return resultStrs;
        }


        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);

            imgAdapter.add(strings);
            imgAdapter.notifyDataSetChanged();


        }

        @Override
        protected String[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

String identifier=params[0];
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url ;
                if(identifier=="P")
                url= new URL("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+API_KEY);
                 else if(identifier=="R")
                url=new URL("https://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key="+API_KEY);
                else
                    url= new URL("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+API_KEY);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Movies string: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            }

            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMoviesList(forecastJsonStr, 7);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
    }
}
