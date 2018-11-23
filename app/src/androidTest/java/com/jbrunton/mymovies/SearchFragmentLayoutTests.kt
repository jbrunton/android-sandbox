package com.jbrunton.mymovies

import android.widget.ProgressBar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.jbrunton.entities.models.LoadingState
import com.jbrunton.entities.models.Movie
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.fixtures.BaseFragmentTest
import com.jbrunton.mymovies.fixtures.FragmentTestRule
import com.jbrunton.mymovies.fixtures.ProgressBarViewActions
import com.jbrunton.mymovies.fixtures.RecyclerViewUtils.withRecyclerView
import com.jbrunton.mymovies.search.SearchFragment
import com.jbrunton.mymovies.search.SearchViewState
import com.jbrunton.mymovies.search.SearchViewStateFactory
import com.jbrunton.mymovies.shared.LoadingViewState
import com.jbrunton.mymovies.shared.toViewState
import com.jbrunton.networking.DescriptiveError
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Arrays.asList


@RunWith(AndroidJUnit4::class)
class SearchFragmentLayoutTests : BaseFragmentTest<SearchFragment>() {
    val MOVIE_FACTORY = MovieFactory()
    val MOVIE1 = MOVIE_FACTORY.create()
    val MOVIE2 = MOVIE_FACTORY.create()

    val EMPTY_STATE = SearchViewStateFactory.emptyState
    val LOADING_STATE = LoadingViewState.Loading<SearchViewState>()

    private val NETWORK_ERROR = DescriptiveError("Network Error", true)

    @Test
    fun defaultsToSearchFragment() {
        takeScreenshot()
        onView(withId(R.id.search_query)).check(matches(isDisplayed()))
    }

    @Test
    fun showsEmptySearchState() {
        setViewState(EMPTY_STATE)

        takeScreenshot("showsEmptySearchState")
        onView(withId(R.id.error_text))
                // TODO: externalize strings
                .check(matches(withText(EMPTY_STATE.errorMessage)))
    }

    @Test
    fun showsLoadingState() {
        onView(isAssignableFrom(ProgressBar::class.java)).perform(ProgressBarViewActions.replaceProgressBarDrawable())

        setViewState(LOADING_STATE)

        takeScreenshot("showsLoadingState")
        onView(withId(R.id.loading_indicator))
                .check(matches(isDisplayed()))
    }

    @Test
    fun showsErrorState() {
        setViewState(LoadingViewState.fromError(NETWORK_ERROR))

        takeScreenshot("showsErrorState")
        onView(withId(R.id.error_text))
                .check(matches(withText(NETWORK_ERROR.message)))
        onView(withId(R.id.error_try_again))
                .check(matches(isDisplayed()))
    }

    @Test
    fun showsResults() {
        setViewState(toViewState(asList(MOVIE1, MOVIE2)))

        takeScreenshot()

        onView(withRecyclerView(R.id.movies_list).atPosition(0))
                .check(matches(hasDescendant(withText(MOVIE1.title))))
        onView(withRecyclerView(R.id.movies_list).atPosition(1))
                .check(matches(hasDescendant(withText(MOVIE2.title))))
    }

    override fun createFragmentTestRule(): FragmentTestRule<*, SearchFragment> {
        return FragmentTestRule.create(SearchFragment::class.java)
    }

    private fun setViewState(viewState: LoadingViewState<SearchViewState>) {
        fragmentRule.runOnUiThread { fragment.updateView(viewState) }
    }

    private fun toViewState(movies: List<Movie>): LoadingViewState<SearchViewState> {
        return LoadingState.Success(movies)
                .toViewState(SearchViewStateFactory.Companion::toViewState)
    }
}