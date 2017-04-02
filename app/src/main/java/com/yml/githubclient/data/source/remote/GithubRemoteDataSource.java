package com.yml.githubclient.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.yml.githubclient.data.models.Follower;
import com.yml.githubclient.data.models.GithubUser;
import com.yml.githubclient.data.source.GithubDataSource;
import com.yml.githubclient.api.GithubInterface;

import java.util.List;

import javax.inject.Singleton;
import io.reactivex.Observable;

/**
 * Created by armando_contreras on 4/2/17.
 */

@Singleton
public class GithubRemoteDataSource implements GithubDataSource {
    private static String TAG = GithubRemoteDataSource.class.getSimpleName();
    private GithubInterface mGithubService;

    public GithubRemoteDataSource(GithubInterface githubInterface) {
        mGithubService = githubInterface;
    }

    @Override
    public Observable<List<Follower>> getFollowerList(String username) {
        Log.d(TAG, "getFollowerList");
        return mGithubService.getFollowersAsync(username);
    }

    @Override
    public Observable<GithubUser> getGithubUser(@NonNull String username) {
        return mGithubService.getUserAsync(username);
    }

    @Override
    public void refreshFollowers() {

    }

}
