package com.yml.githubclient.githubuser;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yml.githubclient.R;
import com.yml.githubclient.data.models.GithubUser;
import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by armando_contreras on 4/1/17.
 */

public class UserDetailFragment extends Fragment implements GithubUserContract.UserView {

    public final static String TAG = UserDetailFragment.class.getSimpleName();
    private static final String PARAM_USER_NAME = "param_user_name";
    private GithubUserContract.Presenter mPresenter;
    private String mUserName;

    @Bind(R.id.hero_section)View mTopSection;
    @Bind(R.id.main_section) View mBottomSection;
    @Bind(R.id.top_image) ImageView mBkgImage;
    @Bind(R.id.avatar_image) ImageView mAvatarImage;

    @Bind(R.id.login_tv) TextView mUsernameTextView;
    @Bind(R.id.full_name_tv) TextView mFullNameTextView;
    @Bind(R.id.location_tv) TextView mLocationTextView;
    @Bind(R.id.email_tv) TextView mEmailTextView;
    @Bind(R.id.followers_tv) TextView mFollowersTextView;
    @Bind(R.id.following_tv) TextView mFollowingTextView;
    @Bind(R.id.repos_tv) TextView mRepositoriesTextView;
    //@Bind(R.id.main_content) TextView mEmptyView;

    protected GithubUser mUser;

    public static UserDetailFragment forUser(String username) {
        final UserDetailFragment userDetailsFragment = new UserDetailFragment();
        final Bundle arguments = new Bundle();
        arguments.putString(PARAM_USER_NAME, username);
        userDetailsFragment.setArguments(arguments);
        return userDetailsFragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container,
                false);

        ButterKnife.bind(this, rootView);

        Log.d(TAG, "onCreateView");
        if (getArguments() != null) {
            mUserName = getArguments().getString(PARAM_USER_NAME);
            if (!TextUtils.isEmpty(mUserName)) {
                mPresenter.loadUser(mUserName, true);
            }
        }
        return rootView;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(@NonNull GithubUserContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showUser(GithubUser user){

        Picasso.with(getActivity())
                .load(user.getAvatarUrl())
                .into(mBkgImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) mBkgImage.getDrawable()).getBitmap();
                        Drawable drawable = new BitmapDrawable(blur(imageBitmap));
                        mBkgImage.setImageDrawable(drawable);
                    }
                    @Override
                    public void onError() {
                    }
                });

        Picasso.with(getActivity())
                .load(user.getAvatarUrl())
                .placeholder(getActivity().getResources().getDrawable(R.drawable.android_icon))
                .into(mAvatarImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) mAvatarImage.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        mAvatarImage.setImageDrawable(imageDrawable);
                    }
                    @Override
                    public void onError() {
                        mAvatarImage.setImageResource(R.drawable.android_icon);
                    }
                });

        if (!TextUtils.isEmpty(user.getEmail())) {
            mEmailTextView.setText(user.getEmail());
        }

        if (!TextUtils.isEmpty(user.getFullName())) {
            mFullNameTextView.setText(user.getFullName());
        }

        if (!TextUtils.isEmpty(user.getLoginName())) {
            mUsernameTextView.setText(user.getLoginName());
        }

        if (!TextUtils.isEmpty(user.getLocation())) {
            mLocationTextView.setText(user.getLocation());
        }

        mRepositoriesTextView.setText(String.valueOf(user.getPublicRepos()));
        mFollowersTextView.setText(String.valueOf(user.getFollowers()));
        mFollowingTextView.setText(String.valueOf(user.getFollowing()));
    }

    private static final float BLUR_RADIUS = 25f;

    public Bitmap blur(Bitmap image) {
        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(getActivity());
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    @Override
    public void showLoadingUserError(){

    }

    @Override
    public boolean isActive(){
        return isAdded();
    }


}

