package com.jbrunton.mymovies.ui.search

import android.widget.ProgressBar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.jbrunton.async.AsyncResult
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.fixtures.BaseFragmentTest
import com.jbrunton.mymovies.fixtures.FragmentTestRule
import com.jbrunton.mymovies.fixtures.ProgressBarViewActions
import com.jbrunton.mymovies.fixtures.RecyclerViewUtils.withRecyclerView
import com.jbrunton.mymovies.fixtures.ViewControllerTestFragment
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewStateError
import com.jbrunton.mymovies.usecases.search.SearchState
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SearchViewControllerTest : BaseFragmentTest<SearchViewControllerTest.TestFragment>() {
    val MOVIE_FACTORY = MovieFactory()
    val MOVIE1 = MOVIE_FACTORY.create()
    val MOVIE2 = MOVIE_FACTORY.create()

    val EMPTY_STATE = SearchViewStateFactory.from(AsyncResult.success(SearchState.EmptyQuery))
    val LOADING_STATE = SearchViewStateFactory.from(AsyncResult.loading(null))

    val NETWORK_ERROR = LoadingViewStateError("Network Error", R.drawable.ic_error_outline_black_24dp, true)
    val NETWORK_ERROR_STATE = SearchViewStateFactory.from(AsyncResult.failure(NETWORK_ERROR))

    val SUCCESS_STATE = SearchViewStateFactory.from(AsyncResult.success(SearchState.Some(listOf(MOVIE1, MOVIE2))))

    @Test
    fun showsEmptySearchState() {
        setViewState(EMPTY_STATE)

        takeScreenshot("showsEmptySearchState")
        onView(withId(R.id.error_text))
                .check(matches(withText(EMPTY_STATE.errorText)))
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
        setViewState(NETWORK_ERROR_STATE)

        takeScreenshot("showsErrorState")
        onView(withId(R.id.error_text))
                .check(matches(withText(NETWORK_ERROR.message)))
        onView(withId(R.id.error_try_again))
                .check(matches(isDisplayed()))
    }

    @Test
    fun showsResults() {
        setViewState(SUCCESS_STATE)

        takeScreenshot()

        onView(withRecyclerView(R.id.movies_list).atPosition(0))
                .check(matches(hasDescendant(withText(MOVIE1.title))))
        onView(withRecyclerView(R.id.movies_list).atPosition(1))
                .check(matches(hasDescendant(withText(MOVIE2.title))))
    }

    override fun createFragmentTestRule(): FragmentTestRule<*, TestFragment> {
        return FragmentTestRule.create(TestFragment::class.java)
    }

    private fun setViewState(viewState: LoadingViewState<SearchViewState>) {
        fragmentRule.runOnUiThread { fragment.updateView(viewState) }
    }

    class TestFragment: ViewControllerTestFragment<LoadingViewState<SearchViewState>>() {
        override fun createViewController() = SearchViewController()
    }
}