package lv.maros.secured.password.keeper.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

class PasswordSecretView_orig @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs), View.OnClickListener {

    @Volatile
    private var isPasswordVisible = false

    lateinit var encryptedPassword: String //is set in binding adapter

    private var passwordClickListener: OnPasswordSecretClickListener? = null

    init {
        setOnClickListener(this)
    }

    private fun toggleVisibility() {
        isPasswordVisible = !isPasswordVisible
    }


    /**
     * we call OnPasswordClickListener only when is clicked on the hidden password
     */
    override fun onClick(v: View) {
        toggleVisibility()

        if (isPasswordVisible) {
            passwordClickListener?.let {
                val decryptedPassword = it.invoke(encryptedPassword)
                text = decryptedPassword
            }
        } else {
            text = PASSWORD_TEXT_VIEW_MASK
        }
    }


    fun setPasswordSecretClickListener(clickListener: OnPasswordSecretClickListener) {
        passwordClickListener = clickListener
    }

    companion object {
        const val PASSWORD_TEXT_VIEW_MASK = "********"
    }
}