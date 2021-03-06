package com.jbrunton.mymovies.fixtures.rules

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.test.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter
import com.jbrunton.inject.HasContainer
import com.jbrunton.mymovies.MyMoviesApplication

fun <T : Activity> ActivityTestRule<T>.takeScreenshot(tag: String = "_") {
    ScreenShotter.takeScreenshot(tag, activity)
}

val <T : Activity> ActivityTestRule<T>.application get() =
    (activity?.application ?: InstrumentationRegistry
            .getInstrumentation()
            .getTargetContext()
            .getApplicationContext()) as MyMoviesApplication

val <T : Activity> ActivityTestRule<T>.container get() =
    (activity as? HasContainer)?.container ?: (application as HasContainer).container

fun <A : AppCompatActivity, F : Fragment> FragmentTestRule<A, F>.takeScreenshot(tag: String = "_") {
    ScreenShotter.takeScreenshot(tag, activity)
}

val <A : AppCompatActivity, F : Fragment> FragmentTestRule<A, F>.application get() =
    (activity?.application ?: InstrumentationRegistry
            .getInstrumentation()
            .getTargetContext()
            .getApplicationContext()) as MyMoviesApplication

val <A : AppCompatActivity, F : Fragment> FragmentTestRule<A, F>.container get() =
    (activity as? HasContainer)?.container ?: (application as HasContainer).container
