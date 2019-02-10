package com.jbrunton.mymovies.ui.shared

import com.google.android.material.snackbar.Snackbar

data class SnackbarMessage(
        val message: String,
        val actionLabel: String? = null,
        val action: (() -> Unit)? = null,
        val duration: Int = Snackbar.LENGTH_SHORT
)
