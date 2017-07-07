package com.codepath.apps.restclienttemplate.models;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.Tweet;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class RetweetActivity extends AppCompatActivity {

    EditText etTweetBody;
    Button btTweet;

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Tweet tweet = getIntent().getParcelableExtra("tweet");
        final long retweetID = getIntent().getLongExtra("tweet", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retweet);

        client = TwitterApplication.getRestClient();

        btTweet = (Button) findViewById(R.id.btTweet);
        etTweetBody = (EditText) findViewById(R.id.etTweetBody);

        String body = getIntent().getStringExtra("body");
        etTweetBody.append(body);

        btTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.sendRetweet(String.valueOf(retweetID), new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                          //  etTweetBody.setText(tweet.body);
                            Tweet tweet = Tweet.fromJSON(response);
                            Intent data = new Intent();
                            data.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, data);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
