package com.jbrunton.mymovies.entities.repositories

import com.jbrunton.mymovies.entities.models.Account
import com.jbrunton.mymovies.entities.models.AuthSession

interface AccountRepository {
    val session: AuthSession
    fun account(): DataStream<Account>
    fun login(username: String, password: String): DataStream<AuthSession>
    fun signOut()
}