package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Felix Huang on 9/30/2017.
 */

public class User {
    public String name;
    public long uid;
    public String screenName;

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String profileImageUrl;

    public static User parseJSON(JSONObject jsonObj) throws JSONException {
        User user = new User();
        user.name = jsonObj.getString("name");
        user.uid  = jsonObj.getLong("id");
        user.screenName = jsonObj.getString("screen_name");
        user.profileImageUrl = jsonObj.getString("profile_image_url");

        return user;
    }
}
