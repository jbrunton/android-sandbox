package com.jbrunton.mymovies.features.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.libs.ui.livedata.observe
import com.jbrunton.mymovies.libs.ui.views.BaseFragment
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment<SearchViewModel>() {
    override val viewModel: SearchViewModel by injectViewModel()
    val viewController by lazy { SearchViewController(viewModel) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onCreateLayout() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        viewController.initializeView(this)
    }

    override fun onBindListeners() {
        viewController.bindListeners(coroutineContext)
    }

    override fun onObserveData() {
        viewModel.viewState.observe(viewLifecycleOwner, viewController::updateView)
    }
}
