package com.jbrunton.mymovies.usecases.auth

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.doOnError
import com.jbrunton.async.doOnSuccess
import com.jbrunton.entities.SchedulerFactory
import com.jbrunton.entities.errors.doOnNetworkError
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.networking.parseStatusMessage
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

class LoginUseCase(val repository: AccountRepository, val schedulerFactory: SchedulerFactory) {
    val loginSuccessful = PublishSubject.create<AuthSession>()
    val loginFailure = PublishSubject.create<String>()

    fun login(username: String, password: String): LoginState {
        val loginState = validate(username, password)
        if (loginState.isValid()) {
            repository.login(username, password)
                    .compose(schedulerFactory.apply())
                    .subscribe(this::onLoginResult)
        }
        return loginState
    }

    private fun validate(username: String, password: String) = LoginState(
            usernamePresent = username.length > 0,
            passwordPresent = password.length > 0)

    private fun onLoginResult(result: AsyncResult<AuthSession>) {
        result.doOnSuccess { loginSuccessful.onNext(it.value) }
                .handleNetworkErrors()
                .doOnNetworkError(this::handleNetworkError)
                .doOnError(HttpException::class) {
                    action(this@LoginUseCase::handleAuthFailure) whenever { it.code() == 401 }
                }
    }

    private fun handleNetworkError(result: AsyncResult.Failure<AuthSession>) {
        loginFailure.onNext("Could not connect to server - please check your connection.")
    }

    private fun handleAuthFailure(result: AsyncResult.Failure<AuthSession>) {
        val message = (result.error as HttpException).parseStatusMessage()
        loginFailure.onNext(message)
    }
}