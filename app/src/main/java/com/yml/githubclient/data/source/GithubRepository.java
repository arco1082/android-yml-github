package com.yml.githubclient.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yml.githubclient.data.models.Follower;
import com.yml.githubclient.data.models.GithubUser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import android.support.annotation.NonNull;
import io.reactivex.Observable;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by armando_contreras on 4/2/17.
 */

@Singleton
public class GithubRepository implements GithubDataSource {

    private final GithubDataSource mFollowersRemoteDataSource;

    @Inject
    GithubRepository(@Remote GithubDataSource followersRemoteDataSource) {
        mFollowersRemoteDataSource = followersRemoteDataSource;
    }

    @Override
    public Observable<List<Follower>> getFollowerList(@NonNull String username) {
        return mFollowersRemoteDataSource
                .getFollowerList(username);

    }

    @Override
    public Observable<GithubUser> getGithubUser(@NonNull final String username) {
        checkNotNull(username);
        return mFollowersRemoteDataSource
                .getGithubUser(username);

    }

    @Override
    public void refreshFollowers() {

    }

}

