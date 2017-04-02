package com.yml.githubclient.di;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yml.githubclient.api.GithubInterface;
import com.yml.githubclient.api.GithubService;
import com.yml.githubclient.api.Constants;
import com.yml.githubclient.rx.SchedulerProvider;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by armando_contreras on 4/1/17.
 */

@Module
public class ApplicationModule {

    private final Context mContext;

    public ApplicationModule(Context context) {
        mContext = context;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }

    @Provides
    GithubService provideGithubService(GithubInterface github) {
        return new GithubService(github);
    }

    @Provides
    Gson provideGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    @Provides
    OkHttpClient providesUrlInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        OkHttpClient again = httpClient.build();
        return again;
    }

    @Provides @Singleton
    GithubInterface providesGithubInterface(OkHttpClient client, Gson gson) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.GITHUB_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(GithubInterface.class);
    }

    @Provides @Singleton
    SchedulerProvider providesScheduler() {
        return new SchedulerProvider();
    }

}