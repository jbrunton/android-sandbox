package com.jbrunton.mymovies.ui.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.async.onSuccess
import com.jbrunton.async.zipWith
import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.ui.search.SearchViewState
import com.jbrunton.mymovies.ui.search.SearchViewStateFactory
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.ui.shared.handleNetworkErrors
import com.jbrunton.mymovies.ui.shared.toLoadingViewState
import io.reactivex.rxkotlin.Observables

class DiscoverViewModel internal constructor(
        private val moviesRepository: MoviesRepository,
        private val genresRepository: GenresRepository
) : BaseLoadingViewModel<DiscoverViewState>() {
    override fun start() {
        load()
    }

    fun retry() {
        load()
    }

    private fun load() {
        val nowPlayingStream = moviesRepository.nowPlaying().map(this::handleErrors)
        val popularStream = moviesRepository.popular().map(this::handleErrors)
        val genresStream = genresRepository.genres().map(this::handleGenresResponse)
        val discoverStream = Observables.zip(nowPlayingStream, popularStream, genresStream) {
            nowPlayingResult, popularResult, genresResult -> nowPlayingResult.zipWith(popularResult, this::partial).zipWith(genresResult) {
                partial, genres -> partial(genres)
            }
        }
        discoverStream.compose(applySchedulers())
                .subscribe(this::handleResponse)
    }

    private fun partial(nowPlaying: SearchViewState, popular: SearchViewState): (GenresViewState) -> DiscoverViewState =
            { genres: GenresViewState -> DiscoverViewState(nowPlaying, popular, genres) }

    private fun handleResponse(result: AsyncResult<DiscoverViewState>) {
        val viewState = result.toLoadingViewState(DiscoverViewState(emptyList(), emptyList(), emptyList()))
        this.viewState.postValue(viewState)
    }

    private fun handleErrors(result: AsyncResult<List<Movie>>): AsyncResult<List<MovieSearchResultViewState>> {
        return result.map(SearchViewStateFactory.Companion::toViewState)
                .handleNetworkErrors()
                .onSuccess {
                    SearchViewStateFactory.errorIfEmpty(it.value)
                }
    }

    private fun handleGenresResponse(state: AsyncResult<List<Genre>>): AsyncResult<GenresViewState> {
        return state
                .onSuccess(this::errorIfEmpty)
                .handleNetworkErrors()
    }

    private fun errorIfEmpty(viewState: AsyncResult.Success<GenresViewState>): AsyncResult<GenresViewState> {
        if (viewState.value.isEmpty()) {
            return failure(
                    errorMessage = "Could not load genres at this time",
                    errorIcon = R.drawable.ic_sentiment_dissatisfied_black_24dp,
                    allowRetry = true)
        } else {
            return viewState
        }
    }
}
