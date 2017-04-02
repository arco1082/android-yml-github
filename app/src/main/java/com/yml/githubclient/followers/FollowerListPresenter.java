package com.yml.githubclient.followers;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import com.yml.githubclient.rx.DefaultObserver;
import com.yml.githubclient.data.models.Follower;
import com.yml.githubclient.data.source.GithubRepository;
import com.yml.githubclient.rx.SchedulerProvider;
import java.util.List;
import static com.google.common.base.Preconditions.checkNotNull;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
/**
 * Created by armando_contreras on 4/1/17.
 */

final class FollowerListPresenter implements FollowerListContract.Presenter {
    private static String TAG = FollowerListPresenter.class.getSimpleName();

    private final GithubRepository mGithubRepository;

    private FollowerListContract.FollowerView mFollowersView;

    private boolean mFirstLoad = true;
    private final CompositeDisposable disposables = new CompositeDisposable();
    @NonNull private SchedulerProvider mSchedulerProvider;

    @Inject
    FollowerListPresenter(GithubRepository repository,
                          FollowerListContract.FollowerView view,
                          @NonNull SchedulerProvider schedulerProvider) {
        mGithubRepository = repository;
        mFollowersView = view;
        mSchedulerProvider = checkNotNull(schedulerProvider, "schedulerProvider cannot be null");
        mFollowersView.setPresenter(this);
        mSchedulerProvider = schedulerProvider;
    }

    @Override
    public void subscribe() {
        //loadTasks(false);
    }

    @Override
    public void unsubscribe() {
        this.mFollowersView = null;
        disposables.clear();
    }

    @Inject
    void setupListeners() {
        mFollowersView.setPresenter(this);
    }

    @Override
    public void loadFollowers(String username, boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadFollowers(username, forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadFollowers(String username, boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mFollowersView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mGithubRepository.refreshFollowers();
        }

        disposables.clear();

        Log.d(TAG, "loadUser " + username);

        final Observable<List<Follower>> observable = mGithubRepository
                .getFollowerList(username)
                // Run on a background thread
                .subscribeOn(mSchedulerProvider.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread());

        disposables.add(observable.subscribeWith(new FollowerListObserver()));

    }

    private void processFollowers(List<Follower> followers) {
        if (followers.isEmpty()) {
            processEmptyFollowers();
        } else {
            mFollowersView.showFollowers(followers);
        }
    }

    @Override
    public void onError() {
        mFollowersView.showLoadingFollowersError();
    }

    private void processEmptyFollowers() {
        mFollowersView.showNoFollowers();
    }

    @Override
    public void openGithUserDetails(View view, @NonNull Follower requestedTask) {
        checkNotNull(requestedTask, "requestedTask cannot be null!");
        mFollowersView.showGithubUserDetailsUI(view, requestedTask.getLoginName());
    }

    private final class FollowerListObserver extends DefaultObserver<List<Follower>> {

        @Override public void onComplete() {
            Log.d(TAG, "onNext");
        }

        @Override public void onError(Throwable e) {
            Log.e(TAG, "onError", e);
            FollowerListPresenter.this.onError();
        }

        @Override public void onNext(List<Follower> users) {
            Log.d(TAG, "onNext");
            FollowerListPresenter.this.processFollowers(users);
        }
    }
}
