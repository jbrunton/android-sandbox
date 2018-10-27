package com.jbrunton.mymovies.moviedetails

import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.movies.MovieViewState
import com.jbrunton.mymovies.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.shared.LoadingViewState

class MovieDetailsViewModel(val movieId: String, val repository: MoviesRepository) : BaseLoadingViewModel<MovieViewState>() {
    override fun start() {
        loadDetails()
    }

    fun retry() {
        loadDetails()
    }

    private fun loadDetails() {
        viewState.setValue(LoadingViewState.Loading)
        repository.getMovie(movieId)
                .compose(applySchedulers())
                .subscribe(this::setMovieResponse, this::setErrorResponse)
    }

    private fun setMovieResponse(movie: Movie) {
        viewState.value = LoadingViewState.Success(MovieViewState(movie))
    }
}
