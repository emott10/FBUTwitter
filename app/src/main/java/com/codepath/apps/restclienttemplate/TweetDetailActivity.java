package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetDetailActivity extends AppCompatActivity {

    public ImageView ivProfileImage;
    public TextView tvUserName;
    public TextView tvBody;
    public TextView tvDate;
    public TextView tvName;
    public ImageButton ibReply;
    public ImageButton ibRetweet;
    public ImageButton ibFav;
    public TextView tvRetweetCount;
    public TextView tvFavCount;
    public ImageView ivVerified;
    public ImageView ivMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Tweet");

        ivProfileImage = findViewById(R.id.ivProfileImageDetail);
        tvName = findViewById(R.id.tvNameDetail);
        tvUserName = findViewById(R.id.tvUsernameDetail);
        tvBody = findViewById(R.id.tvBodyDetail);
        tvDate = findViewById(R.id.tvDateDetail);
        ibFav = findViewById(R.id.ibFavDetail);
        ibReply = findViewById(R.id.ibReplyDetail);
        ibRetweet = findViewById(R.id.ibRetweetDetail);
        tvFavCount = findViewById(R.id.tvFavCountDetail);
        tvRetweetCount = findViewById(R.id.tvRetweetCountDetail);
        ivVerified = findViewById(R.id.ivVerifiedDetail);
        ivMedia = findViewById(R.id.ivTweetImageDetails);

        //ivVerified.setVisibility(View.GONE);

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

        //get the data from previous intent
        Intent i = getIntent();
        Tweet tweet = Parcels.unwrap(i.getParcelableExtra("tweet"));
        String date = tweet.createdAt;
        Long id = tweet.uid;

        date = ParseRelativeDate.getRelativeTimeAgo(date);

        //populate the views according to this tweet
        tvName.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvDate.setText(date);
        tvUserName.setText("@" + tweet.user.screenName);
        tvFavCount.setText(tweet.favCount);
        tvRetweetCount.setText(tweet.retweetCount);
        Glide.with(getApplicationContext())
                .load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(),50, 0))
                .into(ivProfileImage);

        if(tweet.user.verified.equals("false")){
            ivVerified.setVisibility(View.GONE);
        }

        if(tweet.mediaUrl != null){
            Glide.with(getApplicationContext())
                    .load(tweet.mediaUrl)
                    .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(),50, 0))
                    .into(ivMedia);
        }
        else {
            ivMedia.setVisibility(View.GONE);
        }
    }
}
