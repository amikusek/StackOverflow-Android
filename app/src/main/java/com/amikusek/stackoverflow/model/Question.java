package com.amikusek.stackoverflow.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Question {

    public String title;
    public String link;
    @SerializedName("question_id")
    public int id;
    @SerializedName("score")
    public Integer votes;
    @SerializedName("answer_count")
    public Integer answerCount;
    @SerializedName("last_activity_date")
    public long lastActivityDate;
    @SerializedName("creation_date")
    public long creationDate;
    @SerializedName("is_answered")
    public boolean isAnswered;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (id != question.id) return false;
        if (lastActivityDate != question.lastActivityDate) return false;
        if (creationDate != question.creationDate) return false;
        if (isAnswered != question.isAnswered) return false;
        if (title != null ? !title.equals(question.title) : question.title != null) return false;
        if (link != null ? !link.equals(question.link) : question.link != null) return false;
        if (votes != null ? !votes.equals(question.votes) : question.votes != null) return false;
        return answerCount != null ? answerCount.equals(question.answerCount) : question.answerCount == null;
    }
}
