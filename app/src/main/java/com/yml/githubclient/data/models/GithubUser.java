package com.yml.githubclient.data.models;

/**
 * Created by armando_contreras on 4/1/17.
 */
public class GithubUser extends Follower {

    private String name;
    private String location;
    private String email;
    private int public_repos;
    private int followers;
    private int following;

    public GithubUser(String login, String avatar, String name, String location, String email, int repos,
                      int followers, int following) {
        super(login, avatar);
        this.name = name;
        this.location = location;
        this.email = email;
        this.public_repos = repos;
        this.followers = followers;
        this.following = following;
    }

    //Getters
    public String getFullName() { return name; }
    public String getLocation() { return location; }
    public String getEmail() { return email; }
    public int getPublicRepos() { return public_repos; }
    public int getFollowers() { return followers; }
    public int getFollowing() { return following; }

}
