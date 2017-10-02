package com.codepath.apps.restclienttemplate.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapter.TweetsAdapter;
import com.codepath.apps.restclienttemplate.data.CurrUser;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetRequest;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    private EndlessRecyclerViewScrollListener scrollListener;
    public static TimelineActivity currActivity;
    
    RecyclerView rvTweets;
    RelativeLayout rlSearch;
    ArrayList<Tweet> tweets;
    public TweetsAdapter tweetAdapter;
    public NetworkInfo activeNetwork;
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        boolean flagConnected = (activeNetwork != null) && activeNetwork.isConnectedOrConnecting();
        return flagConnected;
    }
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currActivity = this;
        setContentView(R.layout.activity_timeline);
        tweets = new ArrayList<>();
        CurrUser.tweets = tweets;
        /*
        if (CurrUser.tweets == null) {
            tweets = new ArrayList<>();
            CurrUser.tweets = tweets;
        } else {
            tweets = CurrUser.tweets;
        }
        */
        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);

        // Create adapter passing in tweet data
        tweetAdapter = new TweetsAdapter(this, tweets);

        // Attach the adapter to the recyclerview to populate items
        rvTweets.setAdapter(tweetAdapter);

        // Set layout manager to position the items
        // StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(layoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                // loadNextDataFromApi(page);
                // Log.d("DEBUG","page=" + page + " c=" + totalItemsCount);
                CurrUser.page = page - 1;
                populateTimeline();
            }
        };

        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        client = TwitterApp.getRestClient();
        populateTimeline();
    }

    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rlSearch.getWindowToken(), 0);
    }
    
    public void populateTimeline() {
        boolean flagNetworkConnected = isNetworkConnected();
        if (flagNetworkConnected == false) {
            Toast.makeText(this, "Network is not available", Toast.LENGTH_LONG).show();
            return;
        }
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                CurrUser.page = CurrUser.page + 1;
                if (CurrUser.page >= CurrUser.MAX_PAGE) {
                    CurrUser.page = 0;
                }
                if (CurrUser.page == 0) {
                    scrollListener.resetState();
                }
                // Log.d("TwitterClient", response.toString());
                tweets.clear();
                try {
                    Tweet tweet = Tweet.parseJSON(response);
                    tweets.add(tweet);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tweetAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());
                tweets.clear();
                try {
                    for (int k = 0; k < response.length(); k++) {
                        JSONObject jsonObj = response.getJSONObject(k);
                        Tweet tweet = Tweet.parseJSON(jsonObj);
                        tweets.add(tweet);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tweetAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    public void updateTweetsAfterNewPost(JSONObject jsonObj) {
        CurrUser.addNewPost(jsonObj);
        tweetAdapter.notifyItemRangeInserted(0, CurrUser.tweets.size());
        tweetAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.miPost) {
            Toast.makeText(this, "post is pressed", Toast.LENGTH_LONG).show();
            TweetRequest request = new TweetRequest();
            request.setStatus("codepath boot camp is fun. " + CurrUser.newPostIndex);
            CurrUser.newPostIndex = CurrUser.newPostIndex + 1;
            client.postTweet(request);
            // Intent intent = new Intent(this, SettingActivity.class);
            // this.startActivity(intent);

            return true;
        }
        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }
}
