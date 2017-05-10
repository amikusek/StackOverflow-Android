package com.amikusek.stackoverflow.main.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.amikusek.stackoverflow.model.Question;
import com.amikusek.stackoverflow.util.StringHelper;
import com.amikusek.stackoverflow.util.converter.DateConverter;
import com.example.amx86.stackoverflow.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionViewHolder> {

    private List<Question> questionsList = new ArrayList<>();
    private QuestionClickListener onQuestionClickListener;

    public QuestionListAdapter(QuestionClickListener onQuestionClickListener) {
        this.onQuestionClickListener = onQuestionClickListener;
    }

    public List<Question> getQuestionList() {
        return questionsList;
    }

    public void setQuestionsList(List<Question> items) {
        questionsList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuestionViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_content, parent, false));
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        Question question = questionsList.get(position);
        Context context = holder.scoreTextView.getContext();

        holder.titleTextView.setText(StringHelper.fromHtml(question.title));
        holder.scoreTextView.setText(String.valueOf(question.votes));
        holder.answersTextView.setText(question.answerCount.toString());
        holder.lastUpdateTextView.setText(context.getResources()
                .getString(R.string.active,
                        DateConverter.getFormattedDateForTimestamp(question.lastActivityDate))
        );
        holder.creationDateTextView.setText(context.getResources()
                .getString(R.string.asked_date,
                        DateConverter.getFormattedDateForTimestamp(questionsList.get(position).creationDate))
        );

        if (question.isAnswered) {
            holder.answersDataContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
            holder.answersTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.answersText.setTextColor(ContextCompat.getColor(context, R.color.white));
        }

        holder.itemView.setOnClickListener(v -> onQuestionClickListener.onClick(question.link));
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public interface QuestionClickListener {
        void onClick(String link);
    }

}
