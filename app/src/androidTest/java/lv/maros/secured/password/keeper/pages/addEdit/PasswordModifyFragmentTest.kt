package lv.maros.secured.password.keeper.pages.addEdit

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import lv.maros.secured.password.keeper.R
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

// TODO - Use FragmentScenario, see: https://github.com/android/android-test/issues/291
@RunWith(AndroidJUnit4::class)
@MediumTest
class PasswordModifyFragmentTest {

    @Test
    fun clickCancelButton_navigateToAddEditFragment() {
        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer<PasswordAddEditFragment>(Bundle(), R.style.AppTheme)

        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        //Click Cancel button
        onView(withId(R.id.addEdit_cancel_button)).perform(click())


        // THEN - Verify that we navigate to the add screen
        Mockito.verify(navController).navigate(
            PasswordAddEditFragmentDirections.actionAddPasswordFragmentToPasswordListFragment()
        )
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