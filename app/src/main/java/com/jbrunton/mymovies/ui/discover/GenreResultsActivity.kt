package com.jbrunton.mymovies.ui.discover

import com.jbrunton.inject.injectViewModel
import com.jbrunton.inject.parametersOf
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.ui.search.SearchResultsAdapter
import com.jbrunton.mymovies.ui.search.SearchViewState
import com.jbrunton.mymovies.ui.shared.BaseActivity
import com.jbrunton.mymovies.ui.shared.LoadingLayoutManager
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import kotlinx.android.synthetic.main.activity_genre_results.*
import kotlinx.android.synthetic.main.layout_loading_state.*

class GenreResultsActivity : BaseActivity<GenreResultsViewModel>() {
    private lateinit var moviesAdapter: SearchResultsAdapter
    private lateinit var loadingLayoutManager: LoadingLayoutManager

    override val viewModel: GenreResultsViewModel by injectViewModel { parametersOf(genreId(), genreName()) }

    override fun onCreateLayout() {
        setContentView(R.layout.activity_genre_results)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle(genreName())

        loadingLayoutManager = LoadingLayoutManager.buildFor(this, movies_list)

        moviesAdapter = SearchResultsAdapter(this, R.layout.item_movie_card_list)
        movies_list.adapter = moviesAdapter
        movies_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
    }

    override fun onBindListeners() {
        error_try_again.setOnClickListener { viewModel.retry() }
    }

    override fun onObserveData() {
        viewModel.viewState.observe(this, this::updateView)
    }

    private fun genreId(): String = intent.extras["GENRE_ID"] as String
    private fun genreName(): String = intent.extras["GENRE_NAME"] as String

    private fun updateView(viewState: LoadingViewState<SearchViewState>) {
        loadingLayoutManager.updateLayout(viewState, moviesAdapter::setDataSource)
    }
}
