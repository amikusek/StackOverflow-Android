package com.amikusek.stackoverflow.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private boolean loading = false;
    private final int PAGINATION_THRESHOLD = 3;
    private LinearLayoutManager linearLayoutManager;

    protected EndlessScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int firstVisibleItem, visibleItemCount, totalItemCount;
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = linearLayoutManager.getItemCount();
        firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

        if (loading && ((firstVisibleItem + visibleItemCount + 1) < totalItemCount)) {
            loading = false;
        }

        boolean loadPage = (firstVisibleItem + visibleItemCount) == totalItemCount - PAGINATION_THRESHOLD;

        if (!loading && loadPage) {
            onLoadMore();
            loading = true;
        }
    }

    public abstract void onLoadMore();
}
