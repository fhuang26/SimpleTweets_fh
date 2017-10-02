package com.codepath.apps.restclienttemplate.data;

import com.codepath.apps.restclienttemplate.models.Tweet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Felix Huang on 10/1/2017.
 */

public class CurrUser {
    public static ArrayList<Tweet> tweets = null;
    public static int page = 0;
    public static final int MAX_PAGE = 100;
    public static int newPostIndex = 0;
    public static void addNewPost(JSONObject jsonObj) {
        Tweet tweet = null;
        try {
            tweet = Tweet.parseJSON(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<Tweet> prev = new ArrayList<>();
        prev.addAll(tweets);

        tweets.clear();
        tweets.add(tweet);
        tweets.addAll(prev);
    }
}
