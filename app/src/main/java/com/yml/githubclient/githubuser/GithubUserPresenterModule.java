package com.yml.githubclient.githubuser;

import dagger.Module;
import dagger.Provides;

/**
 * Created by armando_contreras on 4/2/17.
 */

@Module
public class GithubUserPresenterModule {

    private final GithubUserContract.UserView mView;

    public GithubUserPresenterModule(GithubUserContract.UserView view) {
        mView = view;
    }

    @Provides
    GithubUserContract.UserView provideUserContractView() {
        return mView;
    }

}
