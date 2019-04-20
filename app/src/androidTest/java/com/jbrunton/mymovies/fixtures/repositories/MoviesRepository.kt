package com.jbrunton.mymovies.fixtures.repositories

import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.MoviesRepository

fun MoviesRepository.stubSearch(query: String, results: List<Movie>) {
    (this as TestMoviesRepository).stubSearch(query, results)
}

fun MoviesRepository.stubWith(movies: List<Movie>) {
    (this as TestMoviesRepository).stubWith(movies)
}