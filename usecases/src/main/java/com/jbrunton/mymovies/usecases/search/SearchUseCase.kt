package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.SchedulerFactory
import com.jbrunton.mymovies.entities.errors.handleNetworkErrors
import com.jbrunton.mymovies.entities.repositories.DataStream
import com.jbrunton.mymovies.entities.repositories.FlowDataStream
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class SearchUseCase(
        val repository: MoviesRepository
) {
    private val searches = BroadcastChannel<String>(1)

    val EmptyQueryResult = AsyncResult.success(SearchResult.EmptyQuery)

    fun search(query: String) {
        searches.offer(query)
    }

    @FlowPreview
    fun results(): FlowDataStream<SearchResult> {
        return searches.asFlow()
                .flatMapLatest { doSearch(it) }
                .onStart { EmptyQueryResult }
    }

    private suspend fun doSearch(query: String): FlowDataStream<SearchResult> {
        if (query.isEmpty()) {
            return flowOf(EmptyQueryResult)
        }

        return repository.searchMovies(query)
                .map { SearchResult.from(it).handleNetworkErrors() }
    }
}
