package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.RetweetActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by neeharmb on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    private static String username;
    Context context;
    private TweetAdapterListener mListener;
    private TwitterClient client;

    // define an interface required by the ViewHolder
    public interface TweetAdapterListener {
        public void onItemSelected(View view, int position);
    }
    // pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets, TweetAdapterListener listener) {
        mTweets = tweets;
        mListener = listener;
    }

    // for each row, inflate the layout and cache references into ViewHolder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // get the data according to position
        final Tweet tweet = mTweets.get(position);

        // populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvName.setText(tweet.name);
        holder.tvTimeCreated.setText(tweet.timeStamp);
        holder.tvRtCount.setText(tweet.rtCount.toString());
        holder.tvFavCount.setText(tweet.favCount.toString());

        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
        if (tweet.mediaUrl != null) {
            Glide.with(context).load(tweet.mediaUrl).into(holder.ivMedia);
        }
        else holder.ivMedia.setVisibility(View.GONE);

        holder.ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReplyActivity.class);
                intent.putExtra("username", tweet.name);
                context.startActivity(intent);
            }
        });

        holder.tvBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TweetDetailActivity.class);
                intent.putExtra("tweet", Parcels.wrap(tweet));
                context.startActivity(intent);
            }
        });

        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("screen_name", tweet.name);
                context.startActivity(intent);
            }
        });

        holder.ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent i = new Intent(context, RetweetActivity.class);
                i.putExtra("tweet", tweet.uid);
                i.putExtra("body", tweet.body);
                context.startActivity(i);
            }
        });

        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = TwitterApplication.getRestClient();
                if (!tweet.isFavorited) {
                    client.sendFavorite(String.valueOf(tweet.uid), new JsonHttpResponseHandler() {
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                holder.ivFavorite.setImageResource(R.drawable.ic_vector_heart);
                                Tweet tweet = Tweet.fromJSON(response);
                                holder.tvFavCount.setText(String.valueOf(tweet.favCount));
                                tweet.isFavorited = true;
//                                Intent intent = new Intent(context, HomeTimelineFragment.class);
//                                intent.putExtra("isFavorited", true);
//                                intent.putExtra("position", tweet.position);
//                                context.startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("TwitterClient", responseString);
                            throwable.printStackTrace();
                        }
                    });
                }
                else {
                    client.sendUnFavorite(String.valueOf(tweet.uid), new JsonHttpResponseHandler() {
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                holder.ivFavorite.setImageResource(R.drawable.ic_vector_heart_stroke);
                                Tweet tweet = Tweet.fromJSON(response);
                             //   holder.tvFavCount.setText(String.valueOf(tweet.favCount));
                                tweet.isFavorited = false;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("TwitterClient", responseString);
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder { // DELETED STATIC IDK IF THIS BREAKS EVERYTHING
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvName;
        public TextView tvTimeCreated;
        public ImageView ivReply;
        public TextView tvRtCount;
        public TextView tvFavCount;
        public ImageView ivMedia;
        public ImageView ivRetweet;
        public ImageView ivFavorite;

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvTimeCreated = (TextView) itemView.findViewById(R.id.tvTimeCreated);
            ivReply = (ImageView) itemView.findViewById(R.id.ivReply);
            tvRtCount = (TextView) itemView.findViewById(R.id.tvRtCount);
            tvFavCount = (TextView) itemView.findViewById(R.id.tvFavCount);
            ivMedia = (ImageView) itemView.findViewById(R.id.ivMedia);
            ivRetweet = (ImageView) itemView.findViewById(R.id.ivRetweet);
            ivFavorite = (ImageView)  itemView.findViewById(R.id.ivFavoriteDetails);

            // handle row click event
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        // get the position of row element
                        int position = getAdapterPosition();
                        // fire the listener callback
                        mListener.onItemSelected(view, position);
                    }
                }
            });

            ivRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    ivRetweet.setImageResource(R.drawable.ic_vector_retweet);
                    Intent i = new Intent(context, ComposeActivity.class);
                }
            });

        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

}
