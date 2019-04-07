package com.jbrunton.mymovies.ui.discover

import android.view.View
import com.jbrunton.mymovies.ui.discover.genres.GenresViewState
import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState

data class DiscoverViewState(
        val nowPlayingViewState: List<MovieSearchResultViewState>,
        val popularViewState: List<MovieSearchResultViewState>,
        val genres: GenresViewState,
        val genresVisibility: Int,
        val genreResults: List<MovieSearchResultViewState>,
        val genreResultsVisibility: Int,
        val selectedGenre: String,
        val selectedGenreVisibility: Int
) {
    companion object {
        val Empty = DiscoverViewState(
                nowPlayingViewState = emptyList(),
                popularViewState = emptyList(),
                genres = emptyList(),
                genresVisibility = View.GONE,
                genreResults = emptyList(),
                genreResultsVisibility = View.GONE,
                selectedGenre = "",
                selectedGenreVisibility = View.GONE)
    }
}