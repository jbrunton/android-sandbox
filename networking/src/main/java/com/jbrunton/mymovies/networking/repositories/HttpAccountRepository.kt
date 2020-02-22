package com.jbrunton.mymovies.networking.repositories

import com.jbrunton.mymovies.entities.models.Account
import com.jbrunton.mymovies.entities.models.AuthSession
import com.jbrunton.mymovies.entities.repositories.*
import com.jbrunton.mymovies.networking.resources.account.AccountResponse
import com.jbrunton.mymovies.networking.resources.auth.AuthSessionRequest
import com.jbrunton.mymovies.networking.resources.auth.LoginRequest
import com.jbrunton.mymovies.networking.services.MovieService

class HttpAccountRepository(
        private val service: MovieService,
        private val preferences: ApplicationPreferences
): AccountRepository {
    override var session: AuthSession = AuthSession.EMPTY
        get() {
            field = AuthSession(preferences.sessionId)
            return field
        }
        private set(value) {
            field = value
            preferences.sessionId = value.sessionId
        }

    override suspend fun account(): FlowDataStream<Account> {
        return buildFlowDataStream {
            service.account(session.sessionId).toAccount().apply {
                preferences.accountId = id
            }
        }
    }

    override fun login(username: String, password: String): DataStream<AuthSession> {
        return service.newAuthToken()
                .flatMap {
                    val loginRequest = LoginRequest(
                            username = username,
                            password = password,
                            requestToken = it.requestToken
                    )
                    service.login(loginRequest)
                }.flatMap {
                    val sessionRequest = AuthSessionRequest(
                            requestToken = it.requestToken
                    )
                    service.newSession(sessionRequest)
                }.doAfterNext{
                    session = it
                }.toDataStream()
    }

    override fun signOut() {
        session = AuthSession.EMPTY
    }
}
