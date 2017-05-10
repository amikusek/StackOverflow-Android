package com.amikusek.stackoverflow.main;

import com.amikusek.stackoverflow._base.BasePresenter;
import com.amikusek.stackoverflow.model.Question;
import com.amikusek.stackoverflow.model.QuestionResponse;
import com.amikusek.stackoverflow.main.view_state.MainViewState;

import java.util.List;

import io.reactivex.Single;

class MainContract {

    interface Presenter extends BasePresenter {

        void onQuestionClicked(String link);
        void getQuestions(String query);
        void onRestoreInstanceState(MainViewState currentState, String errorMessage);
        void attachView(View mainView);
        void detachView();
    }

    interface View {

        void showLoading();
        void showError(String text);
        void showContent();
        void showDefaultState();
        void showNoResults();
        void showNoMoreItemsInfo();
        void startDetailsActivity(String link);
        void setQuestions(List<Question> questionsList);
        void addQuestions(List<Question> questionsList);
        void setCurrentPage(int currentPage);
        int getCurrentPage();
    }

    interface Interactor {

        Single<QuestionResponse> getQuestions(String query, int currentPage);
    }
}
