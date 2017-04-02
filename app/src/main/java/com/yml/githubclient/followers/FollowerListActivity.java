package com.yml.githubclient.followers;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import com.yml.githubclient.GithubClientApplication;
import com.yml.githubclient.R;
import com.yml.githubclient.githubuser.UserDetailActivity;
import static com.google.common.base.Preconditions.checkNotNull;
import javax.inject.Inject;

public class FollowerListActivity extends AppCompatActivity implements FollowerListFragment.FollowerListListener {

    public final static String TAG = FollowerListActivity.class.getSimpleName();
    @Inject FollowerListPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_follower_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        FollowerListFragment fragment =
                (FollowerListFragment) getSupportFragmentManager().findFragmentById(R.id.main_content);

        if (fragment == null) {
            // Create the fragment
            fragment = FollowerListFragment.createNew();
            checkNotNull(getSupportFragmentManager());
            checkNotNull(fragment);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content, fragment);
            transaction.commit();
        }

        DaggerFollowerComponent.builder()
                .githubRepositoryComponent(((GithubClientApplication) getApplication()).getGithubRepositoryComponent())
                .followerListPresenterModule(new FollowerListPresenterModule(fragment)).build()
                .inject(this);
    }

    @Override
    public void onFollowerClicked(View view, String username) {

        Intent intentToLaunch = UserDetailActivity.createDefaultIntent(this, username);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    Pair.create(view.findViewById(R.id.name_label), getString(R.string.label_transition)),
                    Pair.create(view.findViewById(R.id.item_image), getString(R.string.avatar_transition)));
            startActivity(intentToLaunch, options.toBundle());
        } else {
            startActivity(intentToLaunch);
        }

    }
}
