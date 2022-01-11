package lv.maros.secured.password.keeper

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import lv.maros.keeper.R
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class KeeperActivityTest {

    @Test
    fun test_editPassword() {
        val activityScenario = ActivityScenario.launch(KeeperActivity::class.java)



        activityScenario.close()
    }

    @Test
    fun test_addPassword() {
        val activityScenario = ActivityScenario.launch(KeeperActivity::class.java)

        onView(withId(R.id.addPassword_Fab)).perform(click())


        activityScenario.close()
    }

}