package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    private static List<Tweet> mTweets;
    static Context context;
    private static TwitterClient client;

//    public static final DiffUtil.ItemCallback<Tweet> DIFF_CALLBACK =
//            new DiffUtil.ItemCallback<Tweet>() {
//                @Override
//                public boolean areItemsTheSame(Tweet oldItem, Tweet newItem) {
//                    return oldItem.uid == newItem.uid;
//                }
//                @Override
//                public boolean areContentsTheSame(Tweet oldItem, Tweet newItem) {
//                    return (oldItem.body.equals(newItem.body)
//                            && oldItem.createdAt.equals(newItem.createdAt)
//                            && oldItem.favCount.equals(newItem.favCount)
//                            && oldItem.retweetCount.equals(newItem.retweetCount)
//                            && oldItem.isFaved.equals(newItem.isFaved)
//                            && oldItem.isRetweeted.equals(newItem.isRetweeted));
//                }
//            };
//
//    public TweetAdapter() {
//        super(DIFF_CALLBACK);
//    }


    //pass in tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets){
        mTweets = tweets;
    }
    //for each row inflate the layout and cache references into ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        client = TimelineActivity.client;
                //TwitterApplication.getRestClient(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    //bind the values on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //get the data according to position
        Tweet tweet = mTweets.get(i);
        String date = tweet.createdAt;

        date = (String) ParseRelativeDate.getRelativeTimeAgo(date);

        //populate the views according to this data
        viewHolder.tvName.setText(tweet.user.name);
        viewHolder.tvBody.setText(tweet.body);
        viewHolder.tvDate.setText(date);
        viewHolder.tvUserName.setText("@" + tweet.user.screenName);
        viewHolder.tvRetweetCount.setText(tweet.retweetCount);
        viewHolder.tvFavCount.setText(tweet.favCount);
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context,50, 0))
                .into(viewHolder.ivProfileImage);

        if(tweet.isFaved.equals("true")){
            viewHolder.ibFav.setImageResource(R.drawable.ic_vector_heart);
        }

        if(tweet.user.verified.equals("false")){
            viewHolder.ivVerified.setVisibility(View.GONE);
        }

        if(tweet.mediaUrl != null){
            Glide.with(context)
                    .load(tweet.mediaUrl)
                    .bitmapTransform(new RoundedCornersTransformation(context,50, 0))
                    .into(viewHolder.ivMedia);
        }
        else {
            viewHolder.ivMedia.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    //create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvDate;
        public TextView tvName;
        public ImageButton ibReply;
        public TextView tvFavCount;
        public TextView tvRetweetCount;
        public ImageView ivVerified;
        public ImageView ivMedia;
        public ImageButton ibRetweet;
        public ImageButton ibFav;

        public ViewHolder(View itemView){
            super(itemView);

            //final Tweet tweet = mTweets.get(getAdapterPosition());

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvUserName = itemView.findViewById(R.id.tvUsername);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvDate = itemView.findViewById(R.id.tvDate);
            ibFav = itemView.findViewById(R.id.ibFav);
            ibReply = itemView.findViewById(R.id.ibReply);
            ibRetweet = itemView.findViewById(R.id.ibRetweet);
            tvFavCount = itemView.findViewById(R.id.tvFavCount);
            tvRetweetCount = itemView.findViewById(R.id.tvRetweetCount);
            ivVerified = itemView.findViewById(R.id.ivVerified);
            ivMedia = itemView.findViewById(R.id.ivTweetImage);
            ibRetweet.setTag(R.drawable.ic_vector_retweet_stroke);


            ibFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mTweets.get(getAdapterPosition()).isFaved.equals("false")) {
                        createFavorite(mTweets.get(getAdapterPosition()));
                        ibFav.setImageResource(R.drawable.ic_vector_heart);
                    }
                    else{
                        destroyFavorite(mTweets.get(getAdapterPosition()));
                        ibFav.setImageResource(R.drawable.ic_vector_heart_stroke);

                    }
                }
            });

            ibRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if((int)ibRetweet.getTag() == R.drawable.ic_vector_retweet_stroke) {
                        ibRetweet.setImageResource(R.drawable.ic_vector_retweet);
                        ibRetweet.setTag(R.drawable.ic_vector_retweet);
                    }
                    else{
                        ibRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
                        ibRetweet.setTag(R.drawable.ic_vector_retweet_stroke);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tweet tweet = mTweets.get(getAdapterPosition());
                    Intent i = new Intent(context, TweetDetailActivity.class);
                    i.putExtra("tweet", Parcels.wrap(tweet));
                    context.startActivity(i);
                }
            });
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

    public static void createFavorite(final Tweet tweet) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.

        client.favorite(tweet.sId, "create", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //tweet.isFaved = "true";
                Log.d("DEBUG", "Creating favorite success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "Creating favorite error: " + errorResponse.toString());
            }
        });
    }

    public static void destroyFavorite(final Tweet tweet) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.

        client.favorite(tweet.sId, "destroy", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //tweet.isFaved = "false";
                Log.d("DEBUG", "Destroying favorite success");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "Destroying favorite error: " + errorResponse.toString());
            }
        });
    }
}

