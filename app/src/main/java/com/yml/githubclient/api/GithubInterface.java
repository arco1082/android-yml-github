package com.yml.githubclient.api;

import com.yml.githubclient.data.models.Follower;
import com.yml.githubclient.data.models.GithubUser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by armando_contreras on 3/31/17.
 */

public interface GithubInterface {
    @GET("users/{uname}/followers")
    @Headers("Content-type: application/json")
    Call<Follower[]> getFollowers(@Path("uname") String uname);

    @GET("users/{uname}")
    @Headers("Content-type: application/json")
    Call<GithubUser> getUser(@Path("uname") String uname);

    @GET("users/{uname}/followers")
    Observable<List<Follower>> getFollowersAsync(@Path("uname") String uname);

    @GET("users/{uname}")
    Observable<GithubUser> getUserAsync(@Path("uname") String uname);

}
