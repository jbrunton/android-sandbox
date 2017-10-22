package com.jbrunton.mymovies.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.DrawableRes;
import android.text.Html;
import android.view.View;

import com.jbrunton.mymovies.BaseViewModel;
import com.jbrunton.mymovies.Movie;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.api.MaybeError;
import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ServiceFactory;
import com.jbrunton.mymovies.converters.MovieConverter;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

public class SearchViewModel extends BaseViewModel {
    private final MutableLiveData<SearchViewState> viewState = new MutableLiveData<>();
    private final MoviesRepository repository;
    private final MovieConverter converter = new MovieConverter();

    public SearchViewModel() {
        repository = new MoviesRepository(ServiceFactory.instance());
    }

    public LiveData<SearchViewState> viewState() {
        return viewState;
    }

    public void performSearch(String query) {
        if (query.isEmpty()) {
            viewState.setValue(converter.errorBuilder()
                    .setErrorMessage("Search")
                    .setErrorIcon(R.drawable.ic_search_black_24dp)
                    .build());
        } else {
            repository.searchMovies(query)
                    .compose(applySchedulers())
                    .subscribe(this::setResponse);
        }
    }

    private void setResponse(MaybeError<List<Movie>> response) {
        response.ifValue(this::setMoviesResponse).elseIfError(this::setErrorResponse);
    }

    private void setMoviesResponse(List<Movie> movies) {
        viewState.setValue(converter.convertMoviesResponse(movies));
    }

    private void setErrorResponse(DescriptiveError error) {
        viewState.setValue(converter.convertErrorResponse(error));
    }


}