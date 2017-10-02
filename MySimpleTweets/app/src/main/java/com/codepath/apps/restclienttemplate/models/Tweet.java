package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Felix Huang on 9/30/2017.
 */

public class Tweet {
    public String body;

    public String getBody() {
        return body;
    }

    public String getRelTime() {
        return relTime;
    }

    public String relTime;
    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public long uid; // database ID for the tweet
    public String createdAt;
    public User user;

    public static Tweet parseJSON (JSONObject jsonObj) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObj.getString("text");
        tweet.uid = jsonObj.getLong("id");
        tweet.createdAt = jsonObj.getString("created_at");
        tweet.user = User.parseJSON(jsonObj.getJSONObject("user"));
        tweet.relTime = getRelativeTimeAgo(tweet.createdAt);
        return tweet;
    }
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
