package com.jbrunton.mymovies.shared.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.libs.ui.controllers.BaseLoadingViewController

abstract class MoviesListViewController : BaseLoadingViewController<SearchViewState>() {
    abstract override val contentView: RecyclerView
    private lateinit var moviesAdapter: MoviesListAdapter

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        moviesAdapter = MoviesListAdapter(context, R.layout.item_movie_card_list, this::onMovieSelected)
        contentView.adapter = moviesAdapter
        contentView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
    }

    override fun updateContentView(viewState: SearchViewState) {
        moviesAdapter.setDataSource(viewState.results)
    }

    abstract fun onMovieSelected(movie: Movie)
}
