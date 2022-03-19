package lv.maros.secured.password.keeper

import android.app.Application
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import fillPasswordAddEditFragmentWithTestData
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
    fun test_addPasswordAndVerify() {
        val activityScenario = ActivityScenario.launch(KeeperActivity::class.java)

        onView(withId(R.id.addPassword_Fab)).perform(click())

        fillPasswordAddEditFragmentWithTestData(TestPasswordInputDataProvider.INPUT_DATA_ALL_GOOD)

        onView(withId(R.id.add_edit_apply_button)).perform(click())



        activityScenario.close()
    }

}