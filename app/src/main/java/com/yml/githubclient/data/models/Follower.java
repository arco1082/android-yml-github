package com.yml.githubclient.data.models;

/**
 * Created by armando_contreras on 4/1/17.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Follower implements Serializable {

    protected String login;
    protected String avatar_url;

    public Follower(String login, String avatar) {
        this.login = login;
        this.avatar_url = avatar;
    }

    //Getters
    public String getLoginName() { return login; }
    public String getAvatarUrl() { return avatar_url; }

    //Setters
    public void setLoginName(String loginName) { this.login = loginName; }
    public void setAvatarUrl(String url) { this.avatar_url = url; }

}
