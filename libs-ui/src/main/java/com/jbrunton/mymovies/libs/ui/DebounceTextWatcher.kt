package com.jbrunton.mymovies.libs.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebounceTextWatcher(
        val scope: CoroutineScope,
        val action: (String) -> Unit
) : TextWatcher {
    private var previousValue = ""
    private var newValue = ""

    companion object {
        val Delay: Long = 300
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        newValue = s.toString().trim()
        if (newValue != previousValue) {
            previousValue = newValue
            waitAndCheck()
        }
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    private fun waitAndCheck() {
        scope.launch {
            delay(Delay)
            if (newValue == previousValue) {
                action(newValue)
            } else {
                return@launch
            }
        }
    }
}

fun EditText.onTextChanged(scope: CoroutineScope, action: (String) -> Unit): Unit =
        addTextChangedListener(DebounceTextWatcher(scope, action))
