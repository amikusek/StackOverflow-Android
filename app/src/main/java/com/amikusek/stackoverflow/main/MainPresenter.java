package com.amikusek.stackoverflow.main;

import com.amikusek.stackoverflow.main.view_state.MainViewState;
import com.amikusek.stackoverflow.network.PaginationHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;
    private MainContract.Interactor interactor = new MainInteractor();

    @Override
    public void attachView(MainContract.View mainView) {
        this.view = mainView;
    }

    @Override
    public void onQuestionClicked(String link) {
        view.startDetailsActivity(link);
    }

    @Override
    public void getQuestions(String query) {
        if (PaginationHelper.hasMore) {
            interactor
                    .getQuestions(query, view.getCurrentPage())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        if (response.getItems().isEmpty()) {
                            view.showNoResults();
                        } else {
                            if (view.getCurrentPage() == 1) {
                                view.setQuestions(response.getItems());
                            } else {
                                view.addQuestions(response.getItems());
                            }
                            PaginationHelper.hasMore = response.hasMore();
                            view.showContent();
                            view.setCurrentPage(view.getCurrentPage() + 1);
                        }
                    }, error -> view.showError(error.getMessage()));
        } else {
            view.showNoMoreItemsInfo();
        }
    }

    @Override
    public void onRestoreInstanceState(MainViewState currentState, String errorMessage) {
        if (currentState == MainViewState.CONTENT) {
            view.showContent();
        } else if (currentState == MainViewState.ERROR) {
            view.showError(errorMessage);
        } else if (currentState == MainViewState.LOADING) {
            view.showLoading();
        } else if (currentState == MainViewState.EMPTY) {
            view.showNoResults();
        } else {
            view.showDefaultState();
        }
    }

    @Override
    public void detachView() {
        view = null;
    }
}
