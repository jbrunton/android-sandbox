package com.jbrunton.mymovies.shared.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jbrunton.entities.models.Movie
import com.jbrunton.libs.ui.BaseLoadingViewController

abstract class MoviesListViewController(
        override val layout: Int
) : BaseLoadingViewController<SearchViewState>() {
    abstract override val contentView: RecyclerView
    private lateinit var moviesAdapter: MoviesListAdapter

    override fun bind(containerView: View) {
        super.bind(containerView)
        moviesAdapter = MoviesListAdapter(context, R.layout.item_movie_card_list, this::onMovieSelected)
        contentView.adapter = moviesAdapter
        contentView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
    }

    override fun updateContentView(viewState: SearchViewState) {
        moviesAdapter.setDataSource(viewState.results)
    }

    abstract fun onMovieSelected(movie: Movie)
}
