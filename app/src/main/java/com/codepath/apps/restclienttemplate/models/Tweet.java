package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {
    //list attributes
    public String body;
    public long uid;
    public String sId;
    public User user;
    public String createdAt;
    public String retweetCount;
    public String favCount;
    public String mediaUrl;
    public String isFaved;
    public String isRetweeted;

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.sId = jsonObject.getString("id_str");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.retweetCount = jsonObject.getString("retweet_count");
        tweet.favCount = jsonObject.getString("favorite_count");
        tweet.isFaved = jsonObject.getString("favorited");
        tweet.isRetweeted = jsonObject.getString("retweeted");

        if(jsonObject.has("extended_entities")){
            String type = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("type");
            if(type.equals("photo")){
                tweet.mediaUrl = jsonObject.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0).getString("media_url_https");
            }
        }

        return tweet;
    }
}