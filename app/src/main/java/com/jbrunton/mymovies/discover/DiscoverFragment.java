package com.jbrunton.mymovies.discover;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jbrunton.mymovies.LoadingStateContext;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.search.SearchResultsAdapter;
import com.jbrunton.mymovies.search.SearchViewState;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiscoverFragment extends Fragment {
    private SearchResultsAdapter nowPlayingAdapter;
    private DiscoverViewModel viewModel;
    @BindView(R.id.discover_content) View discoverContent;
    @BindView(R.id.now_playing) RecyclerView nowPlayingList;
    private LoadingStateContext loadingStateContext = new LoadingStateContext();

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        ButterKnife.bind(this, view);
        ButterKnife.bind(loadingStateContext, view);

        nowPlayingList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        nowPlayingAdapter = new SearchResultsAdapter(getActivity(), R.layout.item_movie_card_grid);
        nowPlayingList.setAdapter(nowPlayingAdapter);

        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(DiscoverViewModel.class);
        viewModel.viewState().observe(this, this::updateView);
    }

    @OnClick(R.id.genres_link)
    public void onGenresClick() {
        Intent intent = new Intent(getActivity(), GenresActivity.class);
        startActivity(intent);
    }

    private void updateView(SearchViewState viewState) {
        discoverContent.setVisibility(toVisibility(viewState.loadingViewState().showContent()));
        nowPlayingAdapter.setDataSource(viewState.movies());
        loadingStateContext.updateView(viewState.loadingViewState());
    }

    protected int toVisibility(boolean show) {
        return show ? View.VISIBLE : View.GONE;
    }
}