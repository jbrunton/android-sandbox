package com.jbrunton.mymovies.nav

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.account.AccountFragment
import com.jbrunton.mymovies.ui.auth.LoginActivity
import com.jbrunton.mymovies.ui.discover.DiscoverFragment
import com.jbrunton.mymovies.ui.main.MainActivity
import com.jbrunton.mymovies.ui.search.SearchFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*

class Navigator(val activity: FragmentActivity) {
    private val resultHandlers = LinkedList<ResultHandler>()

    abstract class ResultHandler(val requestCode: Int) {
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
            if (requestCode == this.requestCode) {
                onResult(resultCode, data)
                return true
            }
            return false
        }

        abstract fun onResult(resultCode: Int, data: Intent?)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val it = resultHandlers.iterator()
        while (it.hasNext()) {
            val handler = it.next()
            if (handler.onActivityResult(requestCode, resultCode, data)) {
                it.remove()
            }
        }
    }

    fun showSearch() {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.content, SearchFragment())
                .commit()
    }

    fun showDiscover() {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.content, DiscoverFragment())
                .commit()
    }

    fun showAccount() {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.content, AccountFragment())
                .commit()
    }

    fun login(): Observable<AuthSession> {
        val observable: PublishSubject<AuthSession> = PublishSubject.create()
        val intent = Intent(activity, LoginActivity::class.java)
        resultHandlers.add(object : ResultHandler(LoginActivity.LOGIN_REQUEST){
            override fun onResult(resultCode: Int, data: Intent?) {
                observable.onNext(LoginActivity.fromIntent(data!!))
            }
        })
        activity.startActivityForResult(intent, LoginActivity.LOGIN_REQUEST)
        return observable
    }

}