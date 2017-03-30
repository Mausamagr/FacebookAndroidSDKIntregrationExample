package example.facebook.com.facebookassignment.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import example.facebook.com.facebookassignment.Model.Response;
import example.facebook.com.facebookassignment.R;

/**
 * Created by mausamkumari on 3/29/17.
 */

public class FbUserAdapter extends RecyclerView.Adapter<FbUserAdapter.FollowerViewHolder> {

    private List<Response> Userslikes;
    private Callback callback;

    public FbUserAdapter() {
        Userslikes = new ArrayList<>();
    }

    public void setFollowers(List<Response> followers) {
        this.Userslikes = followers;
    }

    public void setCallback(FbUserAdapter.Callback callback) {
        this.callback = callback;
    }

    @Override
    public FbUserAdapter.FollowerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        final FbUserAdapter.FollowerViewHolder viewHolder = new FbUserAdapter.FollowerViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FbUserAdapter.FollowerViewHolder holder, int position) {
        final Response user_like = Userslikes.get(position);
        holder.response = user_like;
        Context context = holder.picture.getContext();
        holder.name.setText(user_like.name);
        if (!TextUtils.isEmpty(user_like.picture)) {
            Picasso.with(context).load(user_like.picture).placeholder(R.drawable.placeholder).into(holder.picture);
        }

        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onItemClick(holder.response, holder.itemView);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return Userslikes.size();
    }

    public static class FollowerViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView name;
        public TextView category;
        public Response response;
        public FollowerViewHolder(View view) {
            super(view);
            picture = (ImageView) view.findViewById(R.id.like_image);
            name = (TextView) view.findViewById(R.id.like_name);
            category = (TextView) view.findViewById(R.id.like_category);
        }
    }

    public interface Callback {
        void onItemClick(Response response, View view);
    }
}
