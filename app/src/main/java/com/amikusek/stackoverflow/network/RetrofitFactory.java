package com.amikusek.stackoverflow.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private static final String BASE_URL = "https://api.stackexchange.com";
    private Retrofit retrofit;

    public RetrofitFactory() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public StackOverflowQuestionsApi getQuestionsApi() {
        return retrofit.create(StackOverflowQuestionsApi.class);
    }
}
