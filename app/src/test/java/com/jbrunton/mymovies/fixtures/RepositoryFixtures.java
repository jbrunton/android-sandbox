package com.jbrunton.mymovies.fixtures;

import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.models.Movie;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

import static org.mockito.Mockito.when;

public class RepositoryFixtures {
    public static class FakeMoviesRepositoryDsl {
        protected final MoviesRepository repository;

        private FakeMoviesRepositoryDsl(MoviesRepository repository) {
            this.repository = repository;
        }
    }

    public static class FakeMoviesFindDsl extends FakeMoviesRepositoryDsl {
        private final String id;

        public FakeMoviesFindDsl(MoviesRepository repository, String id) {
            super(repository);
            this.id = id;
        }

        public void toReturn(Movie movie) {
            toReturnDelayed(movie, 0);
        }

        public void toReturnDelayed(Movie movie, int delay) {
            when(repository.getMovie(id)).thenReturn(Observable.just(movie).delay(delay, TimeUnit.SECONDS));
        }

        public void toErrorWith(Throwable throwable) {
            toErrorWithDelayed(throwable, 0);
        }

        public void toErrorWithDelayed(Throwable throwable, int delay) {
            when(repository.getMovie(id)).thenReturn(Observable.<Movie>error(throwable).delay(delay, TimeUnit.SECONDS));
        }
    }

    public static FakeMoviesFindDsl stubFind(MoviesRepository repository, String id) {
        return new FakeMoviesFindDsl(repository, id);
    }
}
