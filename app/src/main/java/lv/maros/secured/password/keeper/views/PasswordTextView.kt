package lv.maros.secured.password.keeper.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import lv.maros.secured.password.keeper.R
import timber.log.Timber

class PasswordTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    @Volatile
    private var isPasswordVisible = false

    private var clickListener: OnPasswordVisibilityClickListener? = null

    var listener: ((Boolean) -> Unit)? = null

    //TODO rework drawables to attributes !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private val iconVisibleOff: Drawable =
        ContextCompat.getDrawable(context, R.drawable.outline_visibility_off_black_18)!!
    private val iconVisible: Drawable =
        ContextCompat.getDrawable(context, R.drawable.outline_visibility_black_18)!!

    private var iconVisibleOffBounds: Rect? = null

    private val onPasswordTouchListener: OnTouchListener =
        OnTouchListener { v, e ->
            v.performClick()
            processOnTouch(v, e)
        }

    private val gestureDetector: GestureDetector

    private val gestureListener: GestureDetector.SimpleOnGestureListener =
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                iconVisibleOffBounds?.let {
                    return if (it.contains(e.x.toInt(), e.y.toInt())) {
                        Timber.d("onSingleTapConfirmed")
                        changeVisibility()
                        //clickListener?.onClick(isPasswordVisible)
                        listener?.invoke(isPasswordVisible)
                        invalidate()
                        true
                    } else {
                        false
                    }
                }

                return false
            }
        }

    init {
        isClickable = true
        isSingleLine = true
        isPasswordVisible = false

        gestureDetector = GestureDetector(context, gestureListener)
        setOnTouchListener(onPasswordTouchListener)
    }


    private fun processOnTouch(view: View, e: MotionEvent): Boolean {
        val point = Point(e.rawX.toInt(), e.rawY.toInt())

        val rect = Rect()
        view.getGlobalVisibleRect(rect)

        return if (
            e.action == MotionEvent.ACTION_DOWN ||
            e.action == MotionEvent.ACTION_UP ||
            e.action == MotionEvent.ACTION_MOVE
        ) {
            if (rect.top < point.y && rect.bottom > point.y) {
                gestureDetector.onTouchEvent(e)
            } else {
                false
            }
        } else {
            false
        }
    }

    private fun changeVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val icon = if (isPasswordVisible) {
            iconVisible
        } else {
            iconVisibleOff
        }
        icon.bounds = Rect(
            measuredWidth - icon.intrinsicWidth,
            paddingTop,
            measuredWidth,
            measuredHeight
        )

        iconVisibleOffBounds = icon.bounds

        icon.draw(canvas)
    }

    /*fun setOnPasswordVisibilityClickListener(listener: OnPasswordVisibilityClickListener) {
        isClickable = true

        clickListener = listener

    }*/

    fun setOnPasswordVisibilityClickListener(block:(status: Boolean) -> Unit) {
        isClickable = true

        listener = block
    }

    fun runTransformation(f: (String, Int) -> String): String {
        return f("hello", 3)
    }
}

interface OnPasswordVisibilityClickListener {

    fun onClick(isVisible: Boolean)
}