package lv.maros.secured.password.keeper.pages.addEdit

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import fillPasswordAddEditFragmentWithTestData
import hasTextInputLayoutErrorText
import lv.maros.secured.password.keeper.R
import org.junit.Assert
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

        val fragment = launchFragment(navController)

        //Click Cancel button
        onView(withId(R.id.add_edit_cancel_button)).perform(click())

        //Check destination fragment
        Assert.assertEquals(navController.currentDestination!!.id, R.id.passwordsFragment)

        fragment.close()
    }

    @Test
    fun addPassword_passwordsDoNotMatch() {
        val navController = TestNavHostController(appContext)
        val fragment = launchFragment(navController)

        fillPasswordAddEditFragmentWithTestData(
            TestPasswordInputDataProvider.INPUT_DATA_PASSWORDS_DO_NOT_MATCH
        )

        //Click Save button
        onView(withId(R.id.add_edit_apply_button)).perform(click())

        //Check password error
        onView(withId(R.id.add_edit_password_text_layout)).check(
            ViewAssertions.matches(
                hasTextInputLayoutErrorText(
                    appContext.getString(R.string.password_do_not_match_error)
                )
            )
        )
        //Check password error
        onView(withId(R.id.add_edit_repeat_password_text_layout)).check(
            ViewAssertions.matches(
                hasTextInputLayoutErrorText(
                    appContext.getString(R.string.password_do_not_match_error)
                )
            )
        )

        fragment.close()

    }

    private fun launchFragment(navController: TestNavHostController):
            FragmentScenario<PasswordAddEditFragment> {

        val scenario = launchFragmentInContainer<PasswordAddEditFragment>(
            Bundle(),
            R.style.AppTheme
        ).onFragment {
            navController.setGraph(R.navigation.main_nav_graph)
            navController.setCurrentDestination(R.id.passwordAddEditFragment)
            Navigation.setViewNavController(it.requireView(), navController)
        }

        return scenario
    }


    private fun getArgumentsBundle(mode: Int): Bundle {
        val args = Bundle().apply {
            putInt(PasswordAddEditFragment.ARGUMENTS_MODE_KEY, mode)
        }

        return args
    }

}