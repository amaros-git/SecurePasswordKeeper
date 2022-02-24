package lv.maros.secured.password.keeper.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import timber.log.Timber

class PasswordTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs), View.OnClickListener {

    @Volatile
    private var isPasswordVisible = false

    lateinit var encryptedPassword: String

    private var passwordClickListener: OnPasswordClickListener? = null

    init {
        setOnClickListener(this)
    }

    private fun changeVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    /**
     * we call OnPasswordClickListener only when is clicked on the hidden password
     */
    override fun onClick(v: View?) {
        changeVisibility()

        if (isPasswordVisible) {
            passwordClickListener?.let {
                val decryptedPassword = it.invoke(isPasswordVisible, encryptedPassword)
                Timber.d("decryptedPassword = $decryptedPassword")
                text = decryptedPassword
            }
        } else {
            text = PASSWORD_TEXT_VIEW_MASK
        }
    }


    fun setOnPasswordClickListener(clickListener: OnPasswordClickListener) {
        passwordClickListener = clickListener
    }

    companion object {
        const val PASSWORD_TEXT_VIEW_MASK = "********"
    }
}

typealias OnPasswordClickListener = (Boolean, String) -> String


