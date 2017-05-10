package com.amikusek.stackoverflow.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionResponse {

    private List<Question> items;
    @SerializedName("has_more")
    private boolean hasMore;

    public List<Question> getItems() {
        return items;
    }

    public boolean hasMore() {
        return hasMore;
    }
}
