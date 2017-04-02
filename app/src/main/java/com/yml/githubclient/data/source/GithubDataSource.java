package com.yml.githubclient.data.source;

import android.support.annotation.NonNull;

import com.yml.githubclient.data.models.Follower;
import com.yml.githubclient.data.models.GithubUser;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by armando_contreras on 4/2/17.
 */

public interface GithubDataSource {

    Observable<List<Follower>> getFollowerList(@NonNull String username);

    Observable<GithubUser> getGithubUser(@NonNull String username);

    void refreshFollowers();

}

