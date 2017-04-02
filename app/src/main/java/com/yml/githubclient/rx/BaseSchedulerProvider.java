package com.yml.githubclient.rx;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;


/**
 * Created by armando_contreras on 4/2/17.
 */
public interface BaseSchedulerProvider {

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

}
