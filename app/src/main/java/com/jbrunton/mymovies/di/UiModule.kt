package com.jbrunton.mymovies.di

import com.jbrunton.entities.SchedulerContext
import com.jbrunton.inject.module
import com.jbrunton.mymovies.nav.ResultRouter
import com.jbrunton.mymovies.ui.account.AccountViewModel
import com.jbrunton.mymovies.ui.account.favorites.FavoritesViewModel
import com.jbrunton.mymovies.ui.auth.LoginViewModel
import com.jbrunton.mymovies.ui.discover.DiscoverViewModel
import com.jbrunton.mymovies.ui.discover.GenreResultsViewModel
import com.jbrunton.mymovies.ui.discover.GenresViewModel
import com.jbrunton.mymovies.ui.main.MainViewModel
import com.jbrunton.mymovies.ui.moviedetails.MovieDetailsViewModel
import com.jbrunton.mymovies.ui.search.SearchViewModel
import com.jbrunton.mymovies.usecases.auth.LoginUseCase
import com.jbrunton.mymovies.usecases.discover.DiscoverUseCase
import com.jbrunton.mymovies.usecases.favorites.FavoritesUseCase
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsUseCase
import com.jbrunton.mymovies.usecases.search.SearchUseCase

val UiModule = module {
    single { ResultRouter() }

    factory { (schedulerContext: SchedulerContext) -> SearchUseCase(get(), schedulerContext) }
    factory { DiscoverUseCase(get(), get()) }
    factory { FavoritesUseCase(get()) }
    factory { (schedulerContext: SchedulerContext) -> LoginUseCase(get(), schedulerContext) }
    factory { (movieId: String, schedulerContext: SchedulerContext) -> MovieDetailsUseCase(movieId, get(), get(), schedulerContext) }

    factory { MainViewModel(get()) }
    factory { SearchViewModel(get()) }
    factory { DiscoverViewModel(get()) }
    factory { GenresViewModel(get()) }
    factory { LoginViewModel(get()) }
    factory { AccountViewModel(get()) }
    factory { FavoritesViewModel(get()) }
    factory { (movieId: String) -> MovieDetailsViewModel(movieId, get()) }
    factory { (genreId: String) -> GenreResultsViewModel(genreId, get()) }
}
