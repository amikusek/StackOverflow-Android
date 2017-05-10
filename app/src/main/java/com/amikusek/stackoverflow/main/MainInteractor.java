package com.amikusek.stackoverflow.main;

import com.amikusek.stackoverflow.model.QuestionResponse;
import com.amikusek.stackoverflow.network.RetrofitFactory;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

class MainInteractor implements MainContract.Interactor {

    private static final int PAGE_SIZE = 10;
    private static final String SORT_CRITERIA = "activity";
    private RetrofitFactory retrofitFactory = new RetrofitFactory();

    @Override
    public Single<QuestionResponse> getQuestions(String query, int currentPage) {
        return retrofitFactory
                .getQuestionsApi()
                .loadQuestions(query, currentPage, PAGE_SIZE, SORT_CRITERIA)
                .subscribeOn(Schedulers.io());
    }
}
