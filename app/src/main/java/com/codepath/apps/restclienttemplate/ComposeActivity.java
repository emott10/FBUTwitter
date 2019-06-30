package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    Button send;
    EditText etTweet;
    //Tweet tweet;
    private TwitterClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Compose");

        client = TwitterApplication.getRestClient(ComposeActivity.this);
        send = findViewById(R.id.btnSend);
        etTweet = findViewById(R.id.etTweet);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etTweet.getText().toString();

                if(tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Please enter tweet before submitting!", Toast.LENGTH_LONG).show();
                }

                else if(tweetContent.length() > 140){
                    Toast.makeText(ComposeActivity.this, "Your Tweet is too long, Tweets have to be less than 140 characters", Toast.LENGTH_LONG).show();
                }

                else {
                    client.sendTweet(tweetContent, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                Tweet tweet = Tweet.fromJSON(response);
                                Intent data = new Intent();
                                data.putExtra("tweet", Parcels.wrap(tweet));
                                //set result code and bundle data for response
                                setResult(RESULT_OK, data);
                                //closes the activity, pass data to parent
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            }
        });
    }
}

