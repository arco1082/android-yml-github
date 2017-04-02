package com.yml.githubclient.data.source;

import com.yml.githubclient.di.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by armando_contreras on 4/2/17.
 */

@Singleton
@Component(modules = {GithubRepositoryModule.class, ApplicationModule.class})
public interface GithubRepositoryComponent {

    GithubRepository getGithubRepository();
}
