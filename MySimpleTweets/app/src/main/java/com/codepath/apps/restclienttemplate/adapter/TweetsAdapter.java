package com.codepath.apps.restclienttemplate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Created by Felix Huang on 10/1/2017.
 */

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    private Context mContext;
    private List<Tweet> mTweets;
    Picasso picasso;

    public TweetsAdapter(Context context, List<Tweet> Tweets) {
        mContext = context;
        mTweets = Tweets;
        OkHttpClient client = new OkHttpClient();
        picasso = new Picasso.Builder(context).downloader(new OkHttp3Downloader(client)).build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet tweet = mTweets.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class ViewHolder extends RecyclerView.ViewHolder {

        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView tvUserName;
        private TextView tvRelTime;
        private TextView tvBody;
        private ImageView ivProfileImage;
        private RelativeLayout rlItemTweet;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            super(itemView);

            rlItemTweet = (RelativeLayout) itemView.findViewById(R.id.rlItemTweet);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvRelTime = (TextView) itemView.findViewById(R.id.tvRelTime);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
        }

        // Involves populating data into the item through holder
        public void bind(final Tweet tweet) {
            tvUserName.setText(tweet.getUser().getName());
            tvRelTime.setText(tweet.getRelTime());
            tvBody.setText(tweet.getBody());
            if (tweet.getUser().getProfileImageUrl() != null && !tweet.getUser().getProfileImageUrl().isEmpty()) { // to do : to check more for valid url
                picasso.with(mContext).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
                // Toast.makeText(this.itemView.getContext(), tweet.getThumbNail(), Toast.LENGTH_LONG);
            }

            rlItemTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    if (tweet.getWebUrl() == null || tweet.getWebUrl().isEmpty()) {
                        Toast.makeText(mContext, "Web url is not available", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent = new Intent(mContext, TweetActivity.class);
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    mContext.startActivity(intent);
                    */
                }
            });

        }
    }
}
