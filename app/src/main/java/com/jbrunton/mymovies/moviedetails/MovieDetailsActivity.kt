package com.jbrunton.mymovies.moviedetails

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.PicassoHelper
import com.jbrunton.mymovies.shared.BaseActivity
import com.jbrunton.mymovies.shared.LoadingStateContext
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.content_movie_details.*
import kotlinx.android.synthetic.main.layout_loading_state.*

class MovieDetailsActivity : BaseActivity<MovieDetailsViewModel>() {

    private val picassoHelper = PicassoHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        title = ""
        error_try_again.setOnClickListener { viewModel().retry() }
        bindErrorStateContext(LoadingStateContext())

        viewModel().viewState().observe(this, Observer<MovieDetailsViewState> { this.updateView(it!!) })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun provideViewModel(): MovieDetailsViewModel {
        val factory = MovieDetailsViewModel.Factory(
                intent.getStringExtra("MOVIE_ID"), dependencies().moviesRepository())
        return getViewModel(MovieDetailsViewModel::class.java, factory)
    }

    private fun updateView(viewState: MovieDetailsViewState) {
        updateLoadingView(viewState.loadingViewState())

        title = viewState.movie().title()
        overview.text = viewState.movie().overview()

        picassoHelper.loadImage(this, backdrop, viewState.movie().backdropUrl())
    }
}