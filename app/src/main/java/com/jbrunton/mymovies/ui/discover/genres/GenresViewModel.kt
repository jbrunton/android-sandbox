package com.jbrunton.mymovies.ui.discover.genres

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.onSuccess
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.ui.shared.toLoadingViewState

class GenresViewModel(container: Container) : BaseLoadingViewModel<GenresViewState>(container) {
    val repository: GenresRepository by inject()

    override fun start() {
        loadGenres()
    }

    override fun retry() {
        loadGenres()
    }

    private fun loadGenres() {
        subscribe(repository.genres(), this::setGenresResponse)
    }

    private fun setGenresResponse(state: AsyncResult<List<Genre>>) {
        val viewState = state
                .onSuccess(this::errorIfEmpty)
                .handleNetworkErrors()
                .toLoadingViewState(emptyList())
        this.viewState.postValue(viewState)
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