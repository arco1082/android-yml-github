package com.yml.githubclient.api;

/**
 * Created by armando_contreras on 3/31/17.
 */

/**
 * Bootstrap API service
 */
public class GithubService {

    private GithubInterface mGithub;

    /**
     * Create bootstrap service
     * Default CTOR
     */
    public GithubService() {
    }

    /**
     * Create bootstrap service
     */
    public GithubService(GithubInterface git) {
        this.mGithub = git;
    }

    public GithubInterface getInterface() {
        return mGithub;
    }

}

