package lv.maros.secured.password.keeper.pages.addEdit

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import lv.maros.secured.password.keeper.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

// TODO - Use FragmentScenario, see: https://github.com/android/android-test/issues/291
@RunWith(AndroidJUnit4::class)
@MediumTest
class PasswordModifyFragmentTest {

    private lateinit var appContext: Application

    @Before
    fun init() {
        appContext = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun clickCancelButton_navigateToAddEditFragment() {

        val navController = TestNavHostController(appContext)
        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer <PasswordAddEditFragment>(Bundle(), R.style.AppTheme)

        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        //Click Cancel button
        onView(withId(R.id.addEdit_cancel_button)).perform(click())


    }

    private fun fillPasswordModifyView(testDataType: Int) {
        val (website, username, password, repeatPassword) =
            TestPasswordInputDataProvider.provide(testDataType)

        onView(withId(R.id.websiteEditText)).perform(clearText(), typeText(website))
        onView(withId(R.id.usernameEditText)).perform(clearText(), typeText(username))
        onView(withId(R.id.passwordEditText)).perform(clearText(), typeText(password))
        onView(withId(R.id.repeatPasswordEditText)).perform(clearText(), typeText(repeatPassword),
            closeSoftKeyboard()
        )
    }

}