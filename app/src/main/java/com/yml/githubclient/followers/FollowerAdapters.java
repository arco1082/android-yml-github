package com.yml.githubclient.followers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yml.githubclient.R;
import com.yml.githubclient.data.models.Follower;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by armando_contreras on 4/1/17.
 */

public class FollowerAdapters extends RecyclerView.Adapter<FollowerAdapters.FollowerViewHolder> {

    public final static String TAG = FollowerAdapters.class.getSimpleName();

    private Context mContext;
    private List<Follower> mFollowers = new ArrayList<>();
    private int mRowLayout;
    private final OnItemClickListener mListener;
    private int lastPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(View view, Follower userModel);
    }

    public FollowerAdapters(Context context, int rowLayout, OnItemClickListener listener) {
        this.mContext = context;
        this.mRowLayout = rowLayout;
        this.mListener = listener;
    }

    public void swapList(List<Follower> newList) {
        mFollowers.clear();
        if (newList != null) {
            mFollowers.addAll(newList);
        }
    }

    @Override
    public FollowerAdapters.FollowerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mRowLayout, parent, false);
        return new FollowerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FollowerViewHolder holder, final int position) {
        holder.nameTextView.setText(mFollowers.get(position).getLoginName());

        holder.bind(mFollowers.get(position), mListener);
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    Follower employee = mFollowers.get(position);
                    mListener.onItemClick(v, employee);
                }
            }
        });

        Picasso.with(mContext)
                .load(mFollowers.get(position).getAvatarUrl())
                .placeholder(mContext.getResources().getDrawable(R.drawable.android_icon))
                .into(holder.avaterImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) holder.avaterImage.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        holder.avaterImage.setImageDrawable(imageDrawable);
                    }
                    @Override
                    public void onError() {
                        holder.avaterImage.setImageResource(R.drawable.android_icon);
                        //myImageView.setImageResource(R.drawable.default_image);
                    }
                });
        setAnimation(holder.itemView, position);
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    @Override
    public int getItemCount() {
        return mFollowers.size();
    }

    public static class FollowerViewHolder extends RecyclerView.ViewHolder {

        ImageView avaterImage;
        TextView nameTextView;

        public FollowerViewHolder(View itemView) {
            super(itemView);

            avaterImage = (ImageView) itemView.findViewById(R.id.item_image);
            nameTextView = (TextView) itemView.findViewById(R.id.name_label);
        }

        public void bind(final Follower follower, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, follower);
                }
            });
        }
    }
}
