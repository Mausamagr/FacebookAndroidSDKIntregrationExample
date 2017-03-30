package example.facebook.com.facebookassignment.View;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import example.facebook.com.facebookassignment.Model.Response;
import example.facebook.com.facebookassignment.R;

public class FbUserLikesDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PARAM_ID = "detail:_id";

    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";

    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";

    public static final String VIEW_NAME_HEADER_CATEGORY = "detail:header:category";

    private Response item;
    private ImageView image_view;
    private TextView like_name, like_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_user_likes_detail);

        item = Response.getResponse(getIntent().getIntExtra(EXTRA_PARAM_ID, 0));

        image_view = (ImageView) findViewById(R.id.image_view);
        like_category = (TextView) findViewById(R.id.like_category);
        like_name = (TextView) findViewById(R.id.like_name);
        ViewCompat.setTransitionName(image_view, VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(like_name, VIEW_NAME_HEADER_TITLE);
        ViewCompat.setTransitionName(like_category, VIEW_NAME_HEADER_CATEGORY);
        loadItem();
    }

    private void loadItem() {
        like_name.setText(item.getName());
        like_category.setText(getString(R.string.like_category, item.getCategory()));

        if (!addTransitionListener()) {
            loadFullSizeImage();
        }
    }

    private void loadFullSizeImage() {
        Picasso.with(image_view.getContext())
                .load(item.getPictureUrl())
                .noFade()
                .placeholder(R.drawable.placeholder)
                .into(image_view);
    }

    private boolean addTransitionListener() {
        final Transition transition = getWindow().getSharedElementEnterTransition();

        if (transition != null) {

            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {

                    loadFullSizeImage();
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionResume(Transition transition) {
                    // No-op
                }
            });
            return true;
        }

        return false;
    }
}
