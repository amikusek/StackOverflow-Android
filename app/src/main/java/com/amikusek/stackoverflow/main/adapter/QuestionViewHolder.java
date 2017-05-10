package com.amikusek.stackoverflow.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.amx86.stackoverflow.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class QuestionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    TextView titleTextView;
    @BindView(R.id.score)
    TextView scoreTextView;
    @BindView(R.id.answers)
    TextView answersTextView;
    @BindView(R.id.last_update)
    TextView lastUpdateTextView;
    @BindView(R.id.asked_date)
    TextView creationDateTextView;
    @BindView(R.id.answers_text)
    TextView answersText;
    @BindView(R.id.answers_data_container)
    LinearLayout answersDataContainer;

    QuestionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
