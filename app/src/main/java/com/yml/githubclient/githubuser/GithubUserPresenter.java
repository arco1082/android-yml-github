package com.yml.githubclient.githubuser;

import android.support.annotation.NonNull;
import android.util.Log;

import com.yml.githubclient.rx.DefaultObserver;
import com.yml.githubclient.data.models.GithubUser;
import com.yml.githubclient.data.source.GithubRepository;
import com.yml.githubclient.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by armando_contreras on 4/2/17.
 */

final class GithubUserPresenter implements GithubUserContract.Presenter {
    private static String TAG = GithubUserPresenter.class.getSimpleName();

    private final GithubRepository mGithubRepository;

    private GithubUserContract.UserView mUserView;

    private boolean mFirstLoad = true;
    private final CompositeDisposable disposables = new CompositeDisposable();
    @NonNull private SchedulerProvider mSchedulerProvider;

    @Inject
    GithubUserPresenter(GithubRepository repository,
                        GithubUserContract.UserView userView,
                        @NonNull SchedulerProvider schedulerProvider) {
        mGithubRepository = repository;
        mUserView = userView;
        mUserView.setPresenter(this);
        mSchedulerProvider = schedulerProvider;
    }

    @Inject
    void setupListeners() {
        mUserView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        this.mUserView = null;
        disposables.clear();
    }

    @Override
    public void loadUser(String username, boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadUser(username, forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadUser(String username, boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mUserView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mGithubRepository.refreshFollowers();
        }

        final Observable<GithubUser> observable = mGithubRepository
                .getGithubUser(username)
                // Run on a background thread
                .subscribeOn(mSchedulerProvider.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread());

        disposables.add(observable.subscribeWith(new UserObserver()));

    }

    private void processUser(GithubUser user) {
        if (user == null) {
            // Show a message indicating there are no tasks for that filter type.
            onError();
        } else {
            mUserView.showUser(user);
        }
    }

    @Override
    public void onError() {
        mUserView.showLoadingUserError();
    }

    private final class UserObserver extends DefaultObserver<GithubUser> {

        @Override public void onComplete() {
            Log.d(TAG, "onNext");
        }

        @Override public void onError(Throwable e) {
            Log.e(TAG, "onError", e);
            GithubUserPresenter.this.onError();
        }

        @Override public void onNext(GithubUser user) {
            Log.d(TAG, "onNext");
            GithubUserPresenter.this.processUser(user);
        }
    }
}
