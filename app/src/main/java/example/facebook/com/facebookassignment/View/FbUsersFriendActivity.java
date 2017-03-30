package example.facebook.com.facebookassignment.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import example.facebook.com.facebookassignment.Adapter.FbUserAdapter;
import example.facebook.com.facebookassignment.R;
import example.facebook.com.facebookassignment.Model.Response;

public class FbUsersFriendActivity extends AppCompatActivity {

    private List<Response> responseList;
    private ProgressBar mProgressbar;
    private TextView textView;
    private RecyclerView recyclerView;
    private FbUserAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_users_friend);
        responseList = new ArrayList<>();
        initializeViews();
        startLoadingFriends();

    }

    private void initializeViews() {
        mProgressbar = (ProgressBar) findViewById(R.id.progress);
        textView = (TextView) findViewById(R.id.error_text);
        recyclerView = (RecyclerView) findViewById(R.id.friends_recycler_view);
        mAdapter = new FbUserAdapter();
        recyclerView.setAdapter(mAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridSpaceRecyclerView(getApplicationContext()));

        mAdapter.setCallback(new FbUserAdapter.Callback() {
            @Override
            public void onItemClick(Response response, View view) {
                Intent intent = new Intent(FbUsersFriendActivity.this, FbUserLikesDetailActivity.class);
                intent.putExtra(FbUserLikesDetailActivity.EXTRA_PARAM_ID, response.getId());

                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        FbUsersFriendActivity.this,

                        new Pair<View, String>(view.findViewById(R.id.like_image),
                                FbUserLikesDetailActivity.VIEW_NAME_HEADER_IMAGE),
                        new Pair<View, String>(view.findViewById(R.id.like_name),
                                FbUserLikesDetailActivity.VIEW_NAME_HEADER_TITLE));
                        new Pair<View, String>(view.findViewById(R.id.like_category),
                                FbUserLikesDetailActivity.VIEW_NAME_HEADER_CATEGORY);

                ActivityCompat.startActivity(FbUsersFriendActivity.this, intent, activityOptions.toBundle());
            }
        });

        mProgressbar.setVisibility(View.VISIBLE);
    }

    private void startLoadingFriends() {
        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                getAccessToken(),
                "me?fields=likes{category,name,picture}", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject likesData = response.getJSONObject();
                            JSONObject data = likesData.getJSONObject("likes");
                            JSONArray friendsData = data.getJSONArray("data");
                            int len = friendsData.length();
                            String name, category, picture;
                            for(int i =0;i<len;i++) {
                                JSONObject jsonObject = friendsData.getJSONObject(i);
                                name = jsonObject.getString("name");
                                category = jsonObject.getString("category");
                                picture = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
                                responseList.add(new Response(name, category, picture));
                            }
                            Response.setData(responseList);
                            mAdapter.setFollowers(responseList);
                            mProgressbar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            mAdapter.notifyDataSetChanged();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private AccessToken getAccessToken() {
        AccessToken accesstoken = AccessToken.getCurrentAccessToken();
        return accesstoken;
    }

    class GridSpaceRecyclerView extends RecyclerView.ItemDecoration {
        private int mGridSpace;

        public GridSpaceRecyclerView(Context context) {
            mGridSpace = (int) context.getResources().getDimensionPixelSize(R.dimen.horizontal_margin_grid);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mGridSpace, mGridSpace, mGridSpace, mGridSpace);
        }
    }
}
