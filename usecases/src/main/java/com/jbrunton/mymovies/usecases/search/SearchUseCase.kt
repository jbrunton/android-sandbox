package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.SchedulerFactory
import com.jbrunton.mymovies.entities.errors.handleNetworkErrors
import com.jbrunton.mymovies.entities.repositories.DataStream
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SearchUseCase(
        val repository: MoviesRepository,
        val schedulerFactory: SchedulerFactory
) {
    private val searches = PublishSubject.create<String>()

    val EmptyQueryResult = AsyncResult.success(SearchResult.EmptyQuery)

    fun search(query: String) {
        searches.onNext(query)
    }

    fun results(): DataStream<SearchResult> {
        return searches
                .switchMap(this::doSearch)
                .startWith(EmptyQueryResult)
    }

    private fun doSearch(query: String): DataStream<SearchResult> {
        if (query.isEmpty()) {
            return Observable.just(EmptyQueryResult)
        }

        return repository.searchMovies(query)
                .map { SearchResult.from(it).handleNetworkErrors() }
                .subscribeOn(schedulerFactory.IO)
    }
}
