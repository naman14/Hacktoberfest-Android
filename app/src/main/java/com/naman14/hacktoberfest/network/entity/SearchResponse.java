package com.naman14.hacktoberfest.network.entity;

import java.util.List;

/**
 * Created by naman on 5/10/17.
 */

public class SearchResponse {

    private int totalCount;
    private boolean incomplete_results;

    private List<Issue> items;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isIncomplete_results() {
        return incomplete_results;
    }

    public void setIncomplete_results(boolean incomplete_results) {
        this.incomplete_results = incomplete_results;
    }

    public List<Issue> getItems() {
        return items;
    }

    public void setItems(List<Issue> items) {
        this.items = items;
    }
}
