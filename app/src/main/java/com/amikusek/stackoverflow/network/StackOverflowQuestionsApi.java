package com.amikusek.stackoverflow.network;

import com.amikusek.stackoverflow.model.QuestionResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StackOverflowQuestionsApi {

    @GET("/2.2/search/advanced?site=stackoverflow")
    Single<QuestionResponse> loadQuestions(
            @Query("q") String query,
            @Query("page") int page,
            @Query("pagesize") int pageSize,
            @Query("sort") String sortCriteria
    );
}
