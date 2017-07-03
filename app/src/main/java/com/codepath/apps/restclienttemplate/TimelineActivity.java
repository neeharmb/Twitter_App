package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;

import org.parceler.Parcels;

public class TimelineActivity extends AppCompatActivity {


    HomeTimelineFragment fragmentTweetsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.twittersmalllogo);

        // get the view pager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);

        // set the adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), this));

        // setup the TabLayout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_timeline, menu); // used to be main menu
        return true;
    }

    public void onComposeAction(MenuItem miCompose) {
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(i, 20);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // check request code and result code first
        if (resultCode == RESULT_OK && requestCode == 20) {
            // Use data parameter
            Tweet tweet = (Tweet) Parcels.unwrap(data.getParcelableExtra("tweet"));
           // onCreatedTweet(tweet);
            fragmentTweetsList.onCreatedTweet(tweet);
        }
    }

    public void onProfileView(MenuItem item) {
        // launch the profile view
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

}
