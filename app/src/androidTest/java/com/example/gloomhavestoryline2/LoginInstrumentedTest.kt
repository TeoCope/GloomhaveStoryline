package com.example.gloomhavestoryline2

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gloomhavestoryline2.ui.auth.AuthActivity
import com.example.gloomhavestoryline2.view_model.AuthViewModel

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    private val email = "mario@rossi.it"
    private val password = "mario1234"

    @get:Rule
    var activityRule: ActivityScenarioRule<AuthActivity> = ActivityScenarioRule(AuthActivity::class.java)

    @Test
    fun checkLogin() {
        onView(withId(R.id.emailTextInput))
            .perform(typeText(email), closeSoftKeyboard())

        onView(withId(R.id.passwordTextInput))
            .perform(typeText(password), closeSoftKeyboard())

        onView(withId(R.id.btnLogin))
            .perform(click())

        onView(withId(R.id.linearProgressIndicator))
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkResetPassword() {
        onView(withId(R.id.btnForgotPassword))
            .perform(click())

        onView(withText(R.string.reset_password))
            .check(matches(isDisplayed()))

        onView(withId(R.id.resetPasswordTextInput))
            .perform(typeText(email), closeSoftKeyboard())

        onView(withText("Yes"))
            .perform(click())

        onView(withText("Check your email"))
            .check(matches(isDisplayed()))
    }
}