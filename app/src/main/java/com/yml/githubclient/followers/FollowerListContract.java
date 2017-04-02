package com.yml.githubclient.followers;

import android.support.annotation.NonNull;
import android.view.View;

import com.yml.githubclient.BasePresenter;
import com.yml.githubclient.BaseView;
import com.yml.githubclient.data.models.Follower;

import java.util.List;

/**
 * Created by armando_contreras on 4/2/17.
 */

public interface FollowerListContract {

    interface FollowerView extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showFollowers(List<Follower> followers);

        void showGithubUserDetailsUI(View view, String username);

        void showLoadingFollowersError();

        void showNoFollowers();

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        void loadFollowers(String username, boolean forceUpdate);

        void openGithUserDetails(View view, @NonNull Follower requestedTask);

        void onError();

    }
}
