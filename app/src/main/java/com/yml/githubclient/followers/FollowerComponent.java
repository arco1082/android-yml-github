package com.yml.githubclient.followers;

import com.yml.githubclient.data.source.GithubRepositoryComponent;
import com.yml.githubclient.di.FragmentScoped;

import dagger.Component;

/**
 * Created by armando_contreras on 4/2/17.
 */

@FragmentScoped
@Component(dependencies = GithubRepositoryComponent.class, modules = FollowerListPresenterModule.class)
public interface FollowerComponent {

    void inject(FollowerListActivity activity);

}