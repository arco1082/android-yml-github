package com.yml.githubclient.githubuser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.yml.githubclient.GithubClientApplication;
import com.yml.githubclient.R;
import javax.inject.Inject;

/**
 * Created by armando_contreras on 4/1/17.
 */

public class UserDetailActivity extends AppCompatActivity {

    private static final String INTENT_EXTRA_PARAM_USER_ID = "com.yml.INTENT_PARAM_FOLLOWER_ID";
    private static final String INSTANCE_STATE_PARAM_USER_ID = "com.yml.STATE_PARAM_FOLLOWER_ID";
    private String userId;
    private Toolbar mToolbar;
    @Inject
    GithubUserPresenter mUserPresenter;

    public static Intent createDefaultIntent(Context context, String username) {
        Intent callingIntent = new Intent(context, UserDetailActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_USER_ID, username);
        return callingIntent;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        UserDetailFragment fragment =
                (UserDetailFragment) getSupportFragmentManager().findFragmentById(R.id.main_content);
        if (fragment == null) {
            // Create the fragment
            this.userId = getIntent().getStringExtra(INTENT_EXTRA_PARAM_USER_ID);
            fragment = UserDetailFragment.forUser(userId);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content, fragment);
            transaction.commit();
        }

        DaggerGithubUserComponent.builder()
                .githubRepositoryComponent(((GithubClientApplication) getApplication()).getGithubRepositoryComponent())
                .githubUserPresenterModule(new GithubUserPresenterModule(fragment)).build()
                .inject(this);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putString(INSTANCE_STATE_PARAM_USER_ID, this.userId);
        }
        super.onSaveInstanceState(outState);
    }


}
