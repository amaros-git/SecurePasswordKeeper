package lv.maros.secured.password.keeper.utils

import android.view.View
import androidx.databinding.BindingAdapter
import lv.maros.secured.password.keeper.views.PasswordSecretView
import lv.maros.secured.password.keeper.views.PasswordSecretView.Companion.PASSWORD_TEXT_VIEW_MASK

object BindingAdapters {

    /**
     * Use this binding adapter to show and hide the views using boolean variables
     */
    @BindingAdapter("android:fadeVisible")
    @JvmStatic
    fun setFadeVisible(view: View, visible: Boolean? = true) {
        if (view.tag == null) {
            view.tag = true
            view.visibility = if (visible == true) View.VISIBLE else View.GONE
        } else {
            view.animate().cancel()
            if (visible == true) {
                if (view.visibility == View.GONE)
                    view.fadeIn()
            } else {
                if (view.visibility == View.VISIBLE)
                    view.fadeOut()
            }
        }
    }

    @BindingAdapter("password")
    @JvmStatic
    fun setPassword(view: PasswordSecretView, password: String) {
        view.encryptedPassword = password
        view.text = PASSWORD_TEXT_VIEW_MASK
    }
}