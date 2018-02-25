package com.jbrunton.networking.resources.movies

import com.google.common.base.Optional
import com.jbrunton.entities.Configuration
import com.jbrunton.entities.Movie
import org.joda.time.LocalDate

class MovieSearchResultResource(
        override val id: String,
        override val originalTitle: String,
        override val posterPath: String,
        override val backdropPath: String,
        override val releaseDate: LocalDate?,
        override val voteAverage: String) : BaseMovieResource {

    fun toMovie(config: Configuration): Movie {
        return Movie(
                id = id,
                title = originalTitle,
                posterUrl = config.expandUrl(posterPath),
                backdropUrl = config.expandUrl(backdropPath),
                releaseDate = Optional.fromNullable(releaseDate),
                rating = voteAverage,
                overview = Optional.absent()
        )
    }
}
