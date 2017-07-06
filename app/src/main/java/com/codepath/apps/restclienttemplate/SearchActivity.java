package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.restclienttemplate.fragments.SearchTweetsFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

   // SearchTweetsFragment searchFragment;

//    private TwitterClient client;
    String query;

    private TwitterClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        query = getIntent().getStringExtra("query");
        client = TwitterApplication.getRestClient();

        final SearchTweetsFragment searchTweetsFragment = SearchTweetsFragment.newInstance(query);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // make change
        ft.replace(R.id.flContainer, searchTweetsFragment);
        // commit
        ft.commit();

        client = TwitterApplication.getRestClient();
        client.getSearchQueries(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                searchTweetsFragment.search();
            }
        });

    }



}
