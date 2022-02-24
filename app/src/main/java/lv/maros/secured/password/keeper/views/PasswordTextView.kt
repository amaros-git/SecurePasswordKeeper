package lv.maros.secured.password.keeper.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class PasswordTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    @Volatile
    private var isPasswordVisible = false

    private lateinit var encryptedPassword: String


    init {
        isClickable = true
        isPasswordVisible = false

    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        val newText = if (!isPasswordVisible) {
            encryptedPassword = text.toString() //"cache" encrypted password
            "********"
        } else {
            text
        }
        //Timber.d("text = ${text.toString()}")

        super.setText(newText, type)
    }

    /*private fun processVisibilityClick() {
        listener?.let {
            val data = if (isPasswordVisible) {
                it.invoke(isPasswordVisible, encryptedPassword)
            } else {
                encryptedPassword
            }
            Timber.d("received data = $data")
            text = data
        }
    }*/

    private fun changeVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

}


