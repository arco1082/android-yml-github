package com.yml.githubclient.data.source;

import com.yml.githubclient.data.source.remote.GithubRemoteDataSource;
import com.yml.githubclient.api.GithubInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by armando_contreras on 4/2/17.
 */

@Module
public class GithubRepositoryModule {

    @Singleton
    @Provides
    @Remote
    GithubDataSource provideGithubRemoteDataSource(GithubInterface githubInterface) {
        return new GithubRemoteDataSource(githubInterface);
    }

}
