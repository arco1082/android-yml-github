package com.yml.githubclient;

import android.content.Context;

/**
 * Created by armando_contreras on 4/1/17.
 */

public interface BaseView<T> {

    void setPresenter(T presenter);

}