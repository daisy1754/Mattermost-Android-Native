package com.kilogramm.mattermost.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kilogramm.mattermost.MattermostApp;
import com.kilogramm.mattermost.MattermostPreference;
import com.kilogramm.mattermost.model.entity.FoundMessagesIds;
import com.kilogramm.mattermost.model.entity.Posts;
import com.kilogramm.mattermost.model.entity.SearchParams;
import com.kilogramm.mattermost.model.entity.post.PostRepository;
import com.kilogramm.mattermost.network.ApiMethod;
import com.kilogramm.mattermost.rxtest.BaseRxPresenter;
import com.kilogramm.mattermost.view.BaseActivity;
import com.kilogramm.mattermost.view.search.SearchMessageActivity;

import icepick.State;
import io.realm.Realm;
import io.realm.RealmList;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by melkshake on 03.10.16.
 */

public class SearchMessagePresenter extends BaseRxPresenter<SearchMessageActivity> {

    public static final int REQUEST_SEARCH = 1;

    private MattermostApp mMattermostApp;
    private ApiMethod service;

    private boolean isSearchEmpty;

    @State
    String terms;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        mMattermostApp = MattermostApp.getSingleton();
        service = mMattermostApp.getMattermostRetrofitService();

        restartableFirst(REQUEST_SEARCH,
                () -> service.searchForPosts(MattermostPreference.getInstance().getTeamId(), new SearchParams(terms, true))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                (searchMessageActivity, posts) -> {
                    if (posts.getPosts() == null) {
                        sendShowDefaultVisibility(true);
                        isSearchEmpty = true;
                    } else {
                        Realm realm = Realm.getDefaultInstance();
                        RealmList<FoundMessagesIds> list = new RealmList<>();
                        realm.beginTransaction();
                        for (String s : posts.getPosts().keySet()) {
                            list.add(new FoundMessagesIds(s));
                        }
                        realm.where(FoundMessagesIds.class).findAll().deleteAllFromRealm();
                        realm.insertOrUpdate(list);
                        realm.commitTransaction();
                        PostRepository.add(posts.getPosts().values());
                    }

                    if (!isSearchEmpty) {
                        sendSetRecyclerView(terms);
                    }
                }, (searchMessageActivity1, throwable) -> {
                    sendShowProgressBarVisibility(false);
                    throwable.printStackTrace();
                });
    }

    private void sendShowProgressBarVisibility(Boolean bool){
        createTemplateObservable(bool)
                .subscribe(split((searchMessageActivity, aBoolean) -> searchMessageActivity.progressBarVisibility(bool)));
    }

    private void sendShowSearchResultVisibility(Boolean bool){
        createTemplateObservable(bool)
                .subscribe(split((searchMessageActivity, aBoolean) -> searchMessageActivity.searchResultVisibility(bool)));
    }

    private void sendShowDefaultVisibility(Boolean bool){
        createTemplateObservable(bool)
                .subscribe(split((searchMessageActivity, aBoolean) -> searchMessageActivity.defaultVisibility(bool)));
    }

    private void sendShowDefaultMessageVisibility(Boolean bool){
        createTemplateObservable(bool)
                .subscribe(split((searchMessageActivity, aBoolean) -> searchMessageActivity.defaultMessageVisibility(bool)));
    }

    private void sendHideKeyboard(){
        createTemplateObservable(new Object())
                .subscribe(split((searchMessageActivity, o) -> BaseActivity.hideKeyboard(searchMessageActivity)));
    }

    private void sendSetRecyclerView(String terms){
        createTemplateObservable(terms)
                .subscribe(split(SearchMessageActivity::setRecycleView));
    }



    public void search(String terms) {
        this.terms = terms;
        this.isSearchEmpty = false;
        sendHideKeyboard();
        sendShowProgressBarVisibility(true);
        sendShowSearchResultVisibility(false);
        sendShowDefaultVisibility(false);
        sendShowDefaultMessageVisibility(false);

        start(REQUEST_SEARCH);
    }
}
