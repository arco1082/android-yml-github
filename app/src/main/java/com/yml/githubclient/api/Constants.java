package com.yml.githubclient.api;

import java.util.Locale;

/**
 * Created by armando_contreras on 3/31/17.
 */

public class Constants {
    // Set to true to turn on verbose logging
    public static final boolean LOGV = false;

    // Set to true to turn on debug logging
    public static final boolean LOGD = true;

    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds

    public static String GITHUB_ENDPOINT = "https://api.github.com/";
}