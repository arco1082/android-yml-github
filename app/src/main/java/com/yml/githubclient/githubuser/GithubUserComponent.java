package com.yml.githubclient.githubuser;

import com.yml.githubclient.data.source.GithubRepositoryComponent;
import com.yml.githubclient.di.FragmentScoped;

import dagger.Component;

/**
 * Created by armando_contreras on 4/2/17.
 */

@FragmentScoped
@Component(dependencies = GithubRepositoryComponent.class, modules = GithubUserPresenterModule.class)
public interface GithubUserComponent {

    void inject(UserDetailActivity activity);

}