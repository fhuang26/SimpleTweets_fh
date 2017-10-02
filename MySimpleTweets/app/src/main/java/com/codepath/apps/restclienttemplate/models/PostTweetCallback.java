package com.codepath.apps.restclienttemplate.models;

/**
 * Created by Felix Huang 10/1/2017.
 */
public interface PostTweetCallback {

    void onSuccess(Tweet tweet);

    void onError(Exception e);
}
