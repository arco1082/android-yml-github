package com.yml.githubclient.followers;

import dagger.Module;
import dagger.Provides;

/**
 * Created by armando_contreras on 4/2/17.
 */

@Module
public class FollowerListPresenterModule {

    private final FollowerListContract.FollowerView mView;

    public FollowerListPresenterModule(FollowerListContract.FollowerView view) {
        mView = view;
    }

    @Provides
    FollowerListContract.FollowerView provideFollowerListContractView() {
        return mView;
    }

}