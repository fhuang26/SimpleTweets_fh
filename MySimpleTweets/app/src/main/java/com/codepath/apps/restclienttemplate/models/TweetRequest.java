package com.codepath.apps.restclienttemplate.models;

/**
 * Created by Felix Huang 10/1/2017.
 */
public class TweetRequest {

    private String status; // This is the tweet's text

    private Long inReplyToTweetId; // in_reply_to_status_id

    private Double latitude; // lat

    private Double longitude; // long

    public TweetRequest() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getInReplyToTweetId() {
        return inReplyToTweetId;
    }

    public void setInReplyToTweetId(Long inReplyToTweetId) {
        this.inReplyToTweetId = inReplyToTweetId;
    }
}
