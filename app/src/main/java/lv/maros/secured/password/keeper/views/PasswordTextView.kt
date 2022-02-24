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
    var isPasswordVisible = false



    private lateinit var encryptedPassword: String

    var listener: OnPasswordClickListener? = null

    init {
        isPasswordVisible = false

        setOnClickListener(this)
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

    fun setOnPasswordClickListener(listener: OnPasswordClickListener) {
        this.listener = listener
    }

    override fun onClick(v: View?) {
        Timber.d("Click")
    }
}

typealias OnPasswordClickListener = (Boolean, String) -> String


