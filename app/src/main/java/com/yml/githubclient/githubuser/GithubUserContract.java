package com.yml.githubclient.githubuser;

import android.support.annotation.NonNull;

import com.yml.githubclient.BasePresenter;
import com.yml.githubclient.BaseView;
import com.yml.githubclient.data.models.Follower;
import com.yml.githubclient.data.models.GithubUser;

import java.util.List;

/**
 * Created by armando_contreras on 4/2/17.
 */

public interface GithubUserContract {

    interface UserView extends BaseView<com.yml.githubclient.githubuser.GithubUserContract.Presenter> {
        void setLoadingIndicator(boolean active);

        void showUser(GithubUser user);

        void showLoadingUserError();

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        void loadUser(String username, boolean forceUpdate);

        void onError();

    }
}