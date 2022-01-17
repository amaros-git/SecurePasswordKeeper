package lv.maros.secured.password.keeper

import android.app.Application
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.textfield.TextInputLayout
import lv.maros.secured.password.keeper.models.PasswordInputData
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class KeeperActivityTest {

    private lateinit var appContext: Application

    @Before
    fun init() {
        appContext = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun test_addPassword_passwordsDoNotMatch() {
        val activityScenario = ActivityScenario.launch(KeeperActivity::class.java)

        onView(withId(R.id.addPassword_Fab)).perform(click())

        fillPasswordModifyView(INPUT_DATA_PASSWORD_DO_NOT_MATCH)

        onView(withId(R.id.applyButton)).perform(click())

        onView(withId(R.id.passwordLayout)).check(matches(hasTextInputLayoutErrorText(
            appContext.getString(R.string.password_do_not_match_error))))



        activityScenario.close()
    }

    private fun fillPasswordModifyView(inputDataType: Int) {
        val (website, username, password, repeatPassword) = when (inputDataType) {
            INPUT_DATA_PASSWORD_DO_NOT_MATCH -> {
                PasswordInputData("website", "username", "12:$#@", "12:$#")
            }
            else -> {
                PasswordInputData("website", "username", "password", "repeatedPassword")
            }
        }

        onView(withId(R.id.websiteEditText)).perform(clearText(), typeText(website))
        onView(withId(R.id.usernameEditText)).perform(clearText(), typeText(username))
        onView(withId(R.id.passwordEditText)).perform(clearText(), typeText(password))
        onView(withId(R.id.repeatPasswordEditText)).perform(clearText(), typeText(repeatPassword), closeSoftKeyboard())
    }


    private fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View> = object : TypeSafeMatcher<View>() {

        override fun matchesSafely(item: View?): Boolean {
            if (item !is TextInputLayout) return false
            val error = item.error ?: return false
            return expectedErrorText == error.toString()
        }

        override fun describeTo(description: Description?) { }
    }


    companion object {
        private const val INPUT_DATA_ALL_GOOD = 0
        private const val INPUT_DATA_PASSWORD_DO_NOT_MATCH = 0
    }
}