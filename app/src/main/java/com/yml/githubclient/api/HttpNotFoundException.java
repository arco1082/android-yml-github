package com.yml.githubclient.api;


import retrofit2.adapter.rxjava2.HttpException;

/**
 * Created by armando_contreras on 4/2/17.
 */

public class HttpNotFoundException {

    public final static String SERVER_INTERNET_ERROR =
            "Unable to resolve host \"multimedia.telesurtv.net\": No address associated with hostname";

    private HttpNotFoundException() {}

    public static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }
}
