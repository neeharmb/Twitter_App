package com.codepath.apps.restclienttemplate;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by neeharmb on 6/26/17.
 */

@Parcel
public class Tweet {

    // list out the attributes
    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;
    public String timeStamp;
    public String name;
    public Integer favCount;
    public Integer rtCount;
    public String dateCreated;
    public String location;
    public String mediaUrl;
    public boolean hasEntities;
    public boolean hasMedia;
    public boolean isFavorited;

    public Tweet() {}

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.name = "@" + tweet.user.screenName;
        tweet.timeStamp = " · " + TimeFormatter.getTimeDifference(tweet.createdAt);
        tweet.rtCount = jsonObject.getInt("retweet_count");
        tweet.favCount = jsonObject.getInt("favorite_count");
        tweet.dateCreated = jsonObject.getString("created_at");
        tweet.isFavorited = false;
        tweet.hasEntities = jsonObject.has("entities");
        tweet.hasMedia = jsonObject.getJSONObject("entities").has("media");
        if (tweet.hasEntities && tweet.hasMedia) {
            String url = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url");
            tweet.mediaUrl = url + ":large";
        }
        else tweet.mediaUrl = null;


        return tweet;
    }
}
