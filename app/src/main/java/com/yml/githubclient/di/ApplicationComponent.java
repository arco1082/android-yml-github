package com.yml.githubclient.di;

import android.content.Context;
import com.yml.githubclient.api.GithubInterface;
import com.yml.githubclient.api.GithubService;
import com.yml.githubclient.rx.SchedulerProvider;
import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by armando_contreras on 4/1/17.
 */

@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    //Exposed to sub-graphs.
    Context context();

    GithubService githubService();

    GithubInterface githubInterface();

    SchedulerProvider scheduler();

}
