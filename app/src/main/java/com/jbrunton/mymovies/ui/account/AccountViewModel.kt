package com.jbrunton.mymovies.ui.account

import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.usecases.account.AccountUseCase
import com.jbrunton.mymovies.usecases.nav.NavigationResult

class AccountViewModel(container: Container) : BaseLoadingViewModel<AccountViewState>(container) {
    val useCase: AccountUseCase by inject()

    override fun start() {
        subscribe(useCase.state) {
            viewState.postValue(AccountViewStateFactory.viewState(it))
        }
        subscribe(useCase.navigationRequest) {
            navigationRequest.postValue(it)
        }
        useCase.start(schedulerContext)
    }

    override fun retry() {
        useCase.retry()
    }

    override fun onNavigationResult(result: NavigationResult) {
        useCase.onNavigationResult(result)
    }

    fun signOut() {
        useCase.signOut()
    }

    fun signIn() {
        useCase.signIn()
    }
}