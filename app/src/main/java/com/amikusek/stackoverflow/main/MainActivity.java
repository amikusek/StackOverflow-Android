package com.amikusek.stackoverflow.main;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.amikusek.stackoverflow.details.DetailsActivity;
import com.amikusek.stackoverflow.main.adapter.QuestionListAdapter;
import com.amikusek.stackoverflow.main.view_state.MainViewState;
import com.amikusek.stackoverflow.model.Question;
import com.amikusek.stackoverflow.util.EndlessScrollListener;
import com.example.amx86.stackoverflow.R;
import com.jakewharton.rxbinding2.widget.RxSearchView;

import org.parceler.Parcels;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity
        implements MainContract.View {

    private QuestionListAdapter questionListAdapter;
    private MainContract.Presenter mainPresenter;

    private SearchView searchView;
    private Disposable searchViewEventsDisposable;

    private String lastQuery = "";
    private String errorMessage = "";
    private MainViewState currentViewState;
    private int currentPage = 1;
    private boolean wasRotationPerformed = false;

    public static final String QUESTION_URL_EXTRA_STRING = "QUESTION_URL_EXTRA_STRING";
    private static final String ERROR_MESSAGE_PARCELABLE_STRING = "ERROR_MESSAGE";
    private static final String VIEW_STATE_PARCELABLE_STRING = "VIEW_STATE";
    private static final String LAST_QUERY_PARCELABLE_STRING = "LAST_QUERY";
    private static final String QUESTION_LIST_PARCELABLE_STRING = "QUESTION_LIST";
    private static final String CURRENT_PAGE_PARCELABLE_STRING = "CURRENT_PAGE";

    @BindView(R.id.rvQuestions)
    RecyclerView recyclerView;
    @BindView(R.id.default_view)
    TextView defaultView;
    @BindView(R.id.error_view)
    TextView errorView;
    @BindView(R.id.no_results_view)
    TextView noResultsView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter = new MainPresenter();
        ButterKnife.bind(this);
        mainPresenter.attachView(this);
        setSupportActionBar(toolbar);
        initRecyclerView();
        initSwipeRefreshingLayout();

        if (savedInstanceState != null) {
            wasRotationPerformed = true;
            errorMessage = savedInstanceState.getString(ERROR_MESSAGE_PARCELABLE_STRING);
            currentViewState = (MainViewState) savedInstanceState.getSerializable(VIEW_STATE_PARCELABLE_STRING);
            lastQuery = savedInstanceState.getString(LAST_QUERY_PARCELABLE_STRING);
            currentPage = savedInstanceState.getInt(CURRENT_PAGE_PARCELABLE_STRING);

            if (currentViewState == MainViewState.CONTENT) {
                setQuestions(Parcels.unwrap(savedInstanceState.getParcelable(QUESTION_LIST_PARCELABLE_STRING)));
            }
        }

        mainPresenter.onRestoreInstanceState(currentViewState, errorMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        setUpSearchViewWithMenu(menu);
        return true;
    }

    private void setUpSearchViewWithMenu(Menu menu) {
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        if (!lastQuery.isEmpty()) {
            searchItem.expandActionView();
            searchView.setQuery(lastQuery, true);
            searchView.clearFocus();
        }

        subscribeOnSearchViewEvents();
    }

    private void subscribeOnSearchViewEvents() {
        searchViewEventsDisposable =
                RxSearchView
                        .queryTextChanges(searchView)
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .map(CharSequence::toString)
                        .filter(query -> {
                            if (wasRotationPerformed) {
                                wasRotationPerformed = false;
                                return false;
                            } else {
                                return !query.isEmpty();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(query -> {
                            showLoading();
                            setCurrentPage(1);
                            mainPresenter.getQuestions(query);
                            lastQuery = query;
                        })
                        .doOnComplete(searchView::clearFocus)
                        .subscribe();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ERROR_MESSAGE_PARCELABLE_STRING, errorMessage);
        outState.putSerializable(VIEW_STATE_PARCELABLE_STRING, currentViewState);
        outState.putString(LAST_QUERY_PARCELABLE_STRING, lastQuery);
        outState.putParcelable(QUESTION_LIST_PARCELABLE_STRING, Parcels.wrap(questionListAdapter.getQuestionList()));
        outState.putInt(CURRENT_PAGE_PARCELABLE_STRING, currentPage);
        super.onSaveInstanceState(outState);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        questionListAdapter = new QuestionListAdapter(link -> mainPresenter.onQuestionClicked(link));
        recyclerView.setAdapter(questionListAdapter);
        recyclerView.addOnScrollListener(new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                setCurrentPage(getCurrentPage() + 1);
                mainPresenter.getQuestions(lastQuery);
            }
        });
    }

    private void initSwipeRefreshingLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            setCurrentPage(1);
            mainPresenter.getQuestions(lastQuery);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void showLoading() {
        recyclerView.setVisibility(View.GONE);
        defaultView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        noResultsView.setVisibility(View.GONE);
        currentViewState = MainViewState.LOADING;
    }

    @Override
    public void showError(String text) {
        defaultView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        noResultsView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(text);
        errorMessage = text;
        currentViewState = MainViewState.ERROR;
    }

    @Override
    public void showContent() {
        defaultView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        noResultsView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        currentViewState = MainViewState.CONTENT;
    }

    @Override
    public void showDefaultState() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        noResultsView.setVisibility(View.GONE);
        defaultView.setVisibility(View.VISIBLE);
        currentViewState = MainViewState.DEFAULT;
    }

    @Override
    public void showNoResults() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        defaultView.setVisibility(View.GONE);
        noResultsView.setVisibility(View.VISIBLE);
        currentViewState = MainViewState.EMPTY;
    }

    @Override
    public void showNoMoreItemsInfo() {
        Toast.makeText(this, R.string.no_more_items, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setQuestions(List<Question> questionsList) {
        questionListAdapter.getQuestionList().clear();
        questionListAdapter.setQuestionsList(questionsList);
    }

    @Override
    public void addQuestions(List<Question> questionsList) {
        questionListAdapter.setQuestionsList(questionsList);
    }

    @Override
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void startDetailsActivity(String link) {
        DetailsActivity.start(this, link);
    }

    @Override
    protected void onDestroy() {
        mainPresenter.detachView();
        if (searchViewEventsDisposable != null && !searchViewEventsDisposable.isDisposed()) {
            searchViewEventsDisposable.dispose();
        }
        super.onDestroy();
    }
}
