import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.google.android.material.textfield.TextInputLayout
import lv.maros.secured.password.keeper.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View> =
    object : TypeSafeMatcher<View>() {
        override fun matchesSafely(item: View): Boolean {
            if (item !is TextInputLayout) return false
            val error = item.error ?: return false

            return expectedErrorText == error.toString()
        }

        override fun describeTo(description: Description?) {}
    }

fun fillPasswordAddEditFragmentWithTestData(testDataType: Int) {
    val (website, username, password, repeatPassword) =
        TestPasswordInputDataProvider.provide(testDataType)

    onView(withId(R.id.add_edit_website_text)).perform(replaceText(website))
    onView(withId(R.id.add_edit_username_text)).perform(replaceText(username))
    onView(withId(R.id.add_edit_password_text)).perform( replaceText(password))
    onView(withId(R.id.add_edit_repeat_password_text)).perform(
        replaceText(repeatPassword),
        closeSoftKeyboard()
    )
}