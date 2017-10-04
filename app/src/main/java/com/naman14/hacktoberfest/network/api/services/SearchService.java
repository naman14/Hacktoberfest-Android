package com.naman14.hacktoberfest.network.api.services;

import com.naman14.hacktoberfest.network.api.ApiEndPoints;
import com.naman14.hacktoberfest.network.entity.SearchResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by naman on 4/10/17.
 */

public interface SearchService {

    @GET(ApiEndPoints.SEARCH + "/issues")
    Observable<SearchResponse> findPRs(@Query("q") String searchQuery);

    @GET(ApiEndPoints.SEARCH + "/issues")
    Observable<SearchResponse> findIssues(@Query("q") String searchQuery);



}
