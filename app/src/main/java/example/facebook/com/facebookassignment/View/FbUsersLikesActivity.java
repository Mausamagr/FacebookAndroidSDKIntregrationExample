package example.facebook.com.facebookassignment.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import example.facebook.com.facebookassignment.Adapter.FbUserAdapter;
import example.facebook.com.facebookassignment.R;
import example.facebook.com.facebookassignment.Model.Response;

public class FbUsersLikesActivity extends AppCompatActivity implements FbUserAdapter.Callback{

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
        if(savedInstanceState != null) {
            responseList = savedInstanceState.getParcelableArrayList("data");
            if(responseList != null && !responseList.isEmpty()) {
                updateUI();
            }
        } else {
            startLoadingFriends();
        }
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

        mAdapter.setCallback(this);

        mProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("data", new ArrayList<Parcelable>(responseList));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(Response response, View view) {
        Intent intent = new Intent(FbUsersLikesActivity.this, FbUserLikesDetailActivity.class);
        intent.putExtra(FbUserLikesDetailActivity.EXTRA_PARAM_ID, response.getId());

        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                FbUsersLikesActivity.this,

                new Pair<View, String>(view.findViewById(R.id.like_image),
                        FbUserLikesDetailActivity.VIEW_NAME_HEADER_IMAGE),
                new Pair<View, String>(view.findViewById(R.id.like_name),
                        FbUserLikesDetailActivity.VIEW_NAME_HEADER_TITLE));
                new Pair<View, String>(view.findViewById(R.id.like_category),
                FbUserLikesDetailActivity.VIEW_NAME_HEADER_CATEGORY);

        ActivityCompat.startActivity(FbUsersLikesActivity.this, intent, activityOptions.toBundle());
    }

    private void startLoadingFriends() {
        GraphRequest request = GraphRequest.newMeRequest(
                getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        parseJsonData(response);
                        updateUI();
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "likes{category,name,picture}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void parseJsonData(GraphResponse response) {
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
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {
        if(!isDestroyed()) {
            mAdapter.setData(responseList);
            mProgressbar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
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
