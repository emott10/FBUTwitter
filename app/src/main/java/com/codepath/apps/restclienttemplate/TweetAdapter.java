package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    private List<Tweet> mTweets;
    Context context;
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
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context,50, 0))
                .into(viewHolder.ivProfileImage);
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
        public ImageButton ibRetweet;
        public ImageButton ibFav;

        public ViewHolder(View itemView){
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvUserName = itemView.findViewById(R.id.tvUsername);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvDate = itemView.findViewById(R.id.tvDate);
            ibFav = itemView.findViewById(R.id.ibFav);
            ibReply = itemView.findViewById(R.id.ibReply);
            ibRetweet = itemView.findViewById(R.id.ibRetweet);

            ibFav.setTag(R.drawable.ic_vector_heart_stroke);
            ibRetweet.setTag(R.drawable.ic_vector_retweet_stroke);
            ibFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if((int)ibFav.getTag() == R.drawable.ic_vector_heart_stroke) {
                        ibFav.setImageResource(R.drawable.ic_vector_heart);
                        ibFav.setTag(R.drawable.ic_vector_heart);
                    }
                    else{
                        ibFav.setImageResource(R.drawable.ic_vector_heart_stroke);
                        ibFav.setTag(R.drawable.ic_vector_heart_stroke);
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
}

