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
import kotlin.properties.Delegates

class PasswordSecretView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs), View.OnTouchListener, Runnable {

    @Volatile
    private var isPasswordVisible = false

    lateinit var encryptedPassword: String //is set in binding adapter

    private var passwordClickListener: OnPasswordSecretClickListener? = null

    private val handler: Handler = Handler(Looper.getMainLooper())
    private var longPressTimeout = ViewConfiguration.getLongPressTimeout().toLong()

    @Volatile
    private var isLongPress = false

    init {
        //setOnClickListener(this)
        setOnTouchListener(this)
    }

    private fun toggleVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    private fun mPerformLongClick() {

    }

    private fun mPerformClick() {
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
                Timber.d("ACTION_DOWN")
                isLongPress = false
                handler.postDelayed(this, longPressTimeout)
            }
            MotionEvent.ACTION_UP -> {
                Timber.d("ACTION_UP")
                handler.removeCallbacks(this)
                if (!isLongPress) {
                    Timber.d("short press")
                    mPerformClick()
                }
            }
        }
        return true
    }

    fun setPasswordSecretClickListener(clickListener: OnPasswordSecretClickListener) {
        passwordClickListener = clickListener
    }


    companion object {
        const val PASSWORD_TEXT_VIEW_MASK = "********"
    }

    override fun run() {
        isLongPress = true
        Timber.d("long press")
        performLongClick()
    }
}

typealias OnPasswordSecretClickListener = (String) -> String


