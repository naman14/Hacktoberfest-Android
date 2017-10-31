package com.naman14.hacktoberfest.network.repository;

import com.naman14.hacktoberfest.utils.Utils;
import com.naman14.hacktoberfest.network.api.GithubApiManager;
import com.naman14.hacktoberfest.network.entity.Issue;
import com.naman14.hacktoberfest.network.entity.SearchResponse;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by naman on 4/10/17.
 */

public class GithubRepository {

    private static final Object sLock = new Object();

    private GithubApiManager apiManager;

    private static GithubRepository sInstance;

    public static GithubRepository getInstance() {
        synchronized (sLock) {
            if (sInstance== null) {
                sInstance = new GithubRepository();
                sInstance.apiManager = new GithubApiManager();
            }
            return sInstance;
        }
    }

    public Observable<List<Issue>> findValidPRs(String username) {
        return apiManager.getSearchApi().findPRs(Utils.getHacktoberfestStatusQuery(username))
                .flatMap(new Func1<SearchResponse, Observable<List<Issue>>>() {
            @Override
            public Observable<List<Issue>> call(SearchResponse response) {
                if (response != null && response.getItems() != null) {
                    return Observable.just(response.getItems());
                } else {
                    return Observable.error(new Throwable("Error fetching data"));
                }
            }
        });
    }



    public Observable<List<Issue>> findIssues(String language, String[] tags, int page) {
        return apiManager.getSearchApi().findIssues(Utils.getHacktoberfestIssuesQuery(language, tags), page)
                .flatMap(new Func1<SearchResponse, Observable<List<Issue>>>() {
                    @Override
                    public Observable<List<Issue>> call(SearchResponse response) {
                        if (response != null && response.getItems() != null) {
                            return Observable.just(response.getItems());
                        } else {
                            return Observable.error(new Throwable("Error fetching data"));
                        }
                    }
                });
    }
}
