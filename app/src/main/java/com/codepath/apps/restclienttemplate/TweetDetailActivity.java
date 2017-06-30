package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class TweetDetailActivity extends AppCompatActivity {

    ImageView ivProfilePicture;
    TextView tvName;
    TextView tvUsername;
    TextView tvTweetBody;
    TextView tvTimeStamp;
    TextView tvDateCreated;
    Context context = getContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);


        ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
        tvName = (TextView) findViewById(R.id.tvName);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvTweetBody = (TextView) findViewById(R.id.tvTweetBody);
        tvTimeStamp = (TextView) findViewById(R.id.tvTimeStamp);
        tvDateCreated = (TextView) findViewById(R.id.tvDateCreated);


        Intent i = getIntent();
        Tweet tweet = (Tweet) Parcels.unwrap(i.getParcelableExtra("tweet"));

        tvName.setText(tweet.user.name);
        tvUsername.setText(tweet.name);
        tvTweetBody.setText(tweet.body);
        tvTimeStamp.setText(tweet.timeStamp);

        String truncatedDate = tweet.dateCreated.substring(0, 10);
        tvDateCreated.setText(truncatedDate);

        Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfilePicture);
    }
}
