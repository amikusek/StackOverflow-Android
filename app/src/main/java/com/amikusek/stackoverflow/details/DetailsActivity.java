package com.amikusek.stackoverflow.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.amx86.stackoverflow.R;
import com.amikusek.stackoverflow.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webView;

    public static void start(Context context, String questionUrl) {
        Intent starter = new Intent(context, DetailsActivity.class);
        starter.putExtra(MainActivity.QUESTION_URL_EXTRA_STRING, questionUrl);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        webView.loadUrl(bundle.getString(MainActivity.QUESTION_URL_EXTRA_STRING, ""));
    }
}
