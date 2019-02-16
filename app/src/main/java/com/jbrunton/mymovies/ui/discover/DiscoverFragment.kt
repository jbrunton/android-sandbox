package com.jbrunton.mymovies.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.inject.inject
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.nav.Navigator
import com.jbrunton.mymovies.ui.search.SearchResultsAdapter
import com.jbrunton.mymovies.ui.shared.BaseFragment
import kotlinx.android.synthetic.main.fragment_discover.*
import kotlinx.android.synthetic.main.layout_loading_state.*

class DiscoverFragment : BaseFragment<DiscoverViewModel>() {
    private lateinit var nowPlayingAdapter: SearchResultsAdapter
    private lateinit var popularAdapter: SearchResultsAdapter

    override val viewModel: DiscoverViewModel by injectViewModel()
    val navigator: Navigator by inject()
    private val viewController: DiscoverViewController by lazy { DiscoverViewController(navigator) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(viewController.layout, container, false)
    }

    override fun onCreateLayout() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        viewController.bind(this)
        viewController.showLoadingIndicator()
    }

    override fun onBindListeners() {
        error_try_again.setOnClickListener { viewModel.retry() }
    }

    override fun onObserveData() {
        viewModel.viewState.observe(viewLifecycleOwner, viewController::updateView)
    }
}
