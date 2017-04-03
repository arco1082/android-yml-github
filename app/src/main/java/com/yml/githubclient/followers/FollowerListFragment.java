package com.yml.githubclient.followers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yml.githubclient.R;
import com.yml.githubclient.data.models.Follower;
import java.util.ArrayList;
import java.util.List;
import static com.google.common.base.Preconditions.checkNotNull;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by armando_contreras on 4/1/17.
 */


public class FollowerListFragment extends Fragment implements SearchView.OnQueryTextListener, FollowerListContract.FollowerView {

    public final static String TAG = FollowerListFragment.class.getSimpleName();
    private static final String INSTANCE_STATE_PARAM_HAS_SEARCHED = "com.yml.STATE_PARAM_HAS_SEARCHED";
    private static final String INSTANCE_STATE_PARAM_SEARCH = "com.yml.INSTANCE_STATE_PARAM_SEARCH";
    private FollowerListContract.Presenter mPresenter;
    private FollowerAdapter mAdapter;
    protected static final int FOLLOWERS_LOADER = 0;

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.empty_view) TextView mEmptyView;
    @Bind(R.id.rl_progress) RelativeLayout mProgress;
    @Bind(R.id.rl_retry) RelativeLayout layoutRetry;
    @Bind(R.id.bt_retry) Button mBtnRetry;

    protected String mSearchText;
    private FollowerListListener mFollowerListListener;

    private boolean mHasSearched;

    /**
     * Interface for listening user list events.
     */
    public interface FollowerListListener {
        void onFollowerClicked(View view, final String username);
    }

    public static FollowerListFragment createNew() {
        FollowerListFragment fragment = new FollowerListFragment();
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.recycler_view_fragment, container, false);
        ButterKnife.bind(this, fragmentView);
        setupRecyclerView();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mHasSearched) {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FollowerListListener) {
            this.mFollowerListListener = (FollowerListListener) context;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        Log.d(TAG, "onSaveInstanceState");
        state.putBoolean(INSTANCE_STATE_PARAM_HAS_SEARCHED, mHasSearched);
        state.putString(INSTANCE_STATE_PARAM_SEARCH, mSearchText);
        super.onSaveInstanceState(state);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle state) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(state);

        if (state != null) {
            mHasSearched = state.getBoolean(INSTANCE_STATE_PARAM_HAS_SEARCHED);
            mSearchText = state.getString(INSTANCE_STATE_PARAM_SEARCH);
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

    }

    private void setupRecyclerView() {
        GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(lLayout);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new FollowerAdapter(getActivity(), R.layout.follower_list_item, onItemClickListener);
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void setPresenter(@NonNull FollowerListContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mRecyclerView.setAdapter(null);
        ButterKnife.unbind(this);
        this.mFollowerListListener = null;
        mPresenter.unsubscribe();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem actionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(actionMenuItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search_hint));
        if (!mHasSearched) {
            searchView.setIconified(false);
        }

        if (mHasSearched && !TextUtils.isEmpty(mSearchText)) {
            mPresenter.loadFollowers(mSearchText, true);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void showFollowers(List<Follower> followers) {
        mAdapter.swapList(followers);
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void showNoFollowers() {
        mRecyclerView.setVisibility(View.GONE);
        mProgress.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showGithubUserDetailsUI(View view, String username) {
        mFollowerListListener.onFollowerClicked(view, username);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingFollowersError() {
        mProgress.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        showMessage(getString(R.string.followers_error_message));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit " + query);
        mHasSearched = true;
        mSearchText = query;
        if (!TextUtils.isEmpty(query)) {
            mPresenter.loadFollowers(query, true);
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG, "onQueryTextChange " + newText);
        return false;
    }


    private FollowerAdapter.OnItemClickListener onItemClickListener =
            new FollowerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Follower model) {
                    if (FollowerListFragment.this.mPresenter != null && model != null) {
                        FollowerListFragment.this.mPresenter.openGithUserDetails(view, model);
                    }
                }

            };

    private static class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder> {

        public final static String TAG = FollowerAdapters.class.getSimpleName();

        private Context mContext;
        private List<Follower> mFollowers = new ArrayList<>();
        private int mRowLayout;
        private final OnItemClickListener mListener;
        private int lastPosition = -1;

        public interface OnItemClickListener {
            void onItemClick(View view, Follower userModel);
        }

        public FollowerAdapter(Context context, int rowLayout, OnItemClickListener listener) {
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
        public FollowerAdapter.FollowerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(mRowLayout, parent, false);
            return new FollowerAdapter.FollowerViewHolder(view);
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
}
