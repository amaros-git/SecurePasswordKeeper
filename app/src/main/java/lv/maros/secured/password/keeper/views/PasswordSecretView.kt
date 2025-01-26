package lv.maros.secured.password.keeper.views

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.appcompat.widget.AppCompatTextView
import timber.log.Timber

class PasswordSecretView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs), View.OnTouchListener, Runnable {

    @Volatile
    private var isPasswordVisible = false

    lateinit var encryptedPassword: String //is set in binding adapter

    private var passwordClickListener: OnPasswordClickListener? = null
    private var passwordLongClickListener: OnPasswordLongClickListener? = null

    private val handler: Handler = Handler(Looper.getMainLooper())
    private var longPressTimeout = ViewConfiguration.getLongPressTimeout().toLong()

    @Volatile
    private var isLongPress = false

    init {
        setOnTouchListener(this)
    }

    private fun toggleVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    private fun mPerformLongClick() {
        Timber.d("Long click")
    }

    private fun mPerformClick() {
        Timber.d("Short click")

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

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isLongPress = false
                handler.postDelayed(this, longPressTimeout)
            }
            MotionEvent.ACTION_UP -> {
                handler.removeCallbacks(this)
                if (!isLongPress) {
                    mPerformClick()
                }
            }
        }
        return true
    }

    fun setPasswordClickListener(clickListener: OnPasswordClickListener) {
        passwordClickListener = clickListener
    }

    fun setPasswordLongClickListener(longClickListener: OnPasswordLongClickListener) {
        passwordLongClickListener = longClickListener
    }


    companion object {
        const val PASSWORD_TEXT_VIEW_MASK = "********"
    }

    override fun run() {
        isLongPress = true
        mPerformLongClick()
    }
}

typealias OnPasswordClickListener = (String) -> String

typealias OnPasswordLongClickListener = (Unit) -> String


