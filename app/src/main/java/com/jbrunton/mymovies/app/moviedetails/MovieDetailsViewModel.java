package com.jbrunton.mymovies.app.moviedetails;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.api.MaybeError;
import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ServiceFactory;
import com.jbrunton.mymovies.app.converters.MovieResultsConverter;
import com.jbrunton.mymovies.app.models.Movie;
import com.jbrunton.mymovies.app.movies.MovieViewState;
import com.jbrunton.mymovies.app.shared.BaseViewModel;
import com.jbrunton.mymovies.app.shared.LoadingViewState;

public class MovieDetailsViewModel extends BaseViewModel {
    private final MutableLiveData<MovieDetailsViewState> viewState = new MutableLiveData<>();
    private final MoviesRepository repository;
    private final MovieResultsConverter converter = new MovieResultsConverter();

    public MovieDetailsViewModel(String movieId) {
        repository = new MoviesRepository(ServiceFactory.instance());
        viewState.setValue(MovieDetailsViewState.builder()
                .setLoadingViewState(LoadingViewState.LOADING_STATE)
                .setMovie(MovieViewState.EMPTY)
                .build());
        repository.getMovie(movieId)
                .compose(applySchedulers())
                .subscribe(this::setResponse);
    }

    public LiveData<MovieDetailsViewState> viewState() {
        return viewState;
    }

    private void setResponse(MaybeError<Movie> response) {
        response.ifValue(this::setMovieResponse).elseIfError(this::setErrorResponse);
    }

    private void setMovieResponse(Movie movie) {
        viewState.setValue(converter.toMovieDetailsViewState(movie));
    }

    private void setErrorResponse(DescriptiveError error) {
        viewState.setValue(converter.toMovieDetailsViewState(error));
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final String movieId;

        public Factory(String movieId) {
            this.movieId = movieId;
        }

        @Override public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MovieDetailsViewModel(movieId);
        }
    }
}