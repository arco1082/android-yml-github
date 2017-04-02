package com.yml.githubclient;

import android.app.Application;
import com.yml.githubclient.data.source.DaggerGithubRepositoryComponent;
import com.yml.githubclient.data.source.GithubRepositoryComponent;
import com.yml.githubclient.di.ApplicationModule;

/**
 * Created by armando_contreras on 4/1/17.
 */

public class GithubClientApplication extends Application {
    private GithubRepositoryComponent mRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
    }

    protected void initializeInjector() {
        mRepositoryComponent = DaggerGithubRepositoryComponent.builder()
                .applicationModule(new ApplicationModule((GithubClientApplication) getApplicationContext()))
                .build();
    }

    public GithubRepositoryComponent getGithubRepositoryComponent() {
        return mRepositoryComponent;
    }

}
