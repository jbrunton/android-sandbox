package com.jbrunton.mymovies.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.helpers.toVisibility
import com.jbrunton.mymovies.shared.BaseFragment
import com.jbrunton.mymovies.shared.LoadingViewState
import com.jbrunton.mymovies.shared.Success
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class SearchFragment : BaseFragment<SearchViewModel>() {
    private lateinit var searchResultsAdapter: SearchResultsAdapter

    val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movies_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        searchResultsAdapter = SearchResultsAdapter(activity!!, R.layout.item_movie_card_list)
        movies_list.adapter = searchResultsAdapter

        search_query.textChanges()
                .debounce(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(this)
                .subscribe { this.performSearch() }

        error_try_again.clicks()
                .bindToLifecycle(this)
                .subscribe { this.performSearch() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(my_toolbar)
        viewModel.viewState.observe(this, this::updateView)
        viewModel.start()
    }

    private fun performSearch() {
        viewModel.performSearch(search_query.text.toString())
    }

    fun updateView(viewState: LoadingViewState<SearchViewState>) {
        movies_list.visibility = toVisibility(viewState is Success)
        if (viewState is Success) {
            searchResultsAdapter.setDataSource(viewState.value)
        }
        updateLoadingView(viewState)
    }
}
