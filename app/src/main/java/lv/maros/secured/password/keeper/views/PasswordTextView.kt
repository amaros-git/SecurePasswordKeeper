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
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import lv.maros.secured.password.keeper.R
import timber.log.Timber

class PasswordTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    @Volatile
    private var isPasswordVisible = true

    //TODO rework drawables to attributes !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private val iconVisibleOff: Drawable =
        ContextCompat.getDrawable(context, R.drawable.outline_visibility_off_black_18)!!
    private val iconVisible: Drawable =
        ContextCompat.getDrawable(context, R.drawable.outline_visibility_black_18)!!

    private var iconVisibleOffBounds: Rect? = null

    private var widthSize = 0
    private var heightSize = 0

    private val onPasswordTouchListener: OnTouchListener =
        View.OnTouchListener { v, e ->
            v.performClick()
            processOnTouch(v, e)
        }

    private lateinit var gestureDetector: GestureDetector

    private val gestureListener: GestureDetector.SimpleOnGestureListener =
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                iconVisibleOffBounds?.let {
                    return if (it.contains(e.x.toInt(), e.y.toInt())) {
                        //passwordClickListener.onDeleteClick(swipedPos)
                        Timber.d("onSingleTapConfirmed")
                        true
                    } else {
                        false
                    }
                }

                return false
            }
        }

    init {
        isClickable = true //TODO
        isSingleLine = true

        gestureDetector = GestureDetector(context, gestureListener)

        Timber.d("measuredWidth = $measuredWidth, measuredHeight = $measuredHeight")

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

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val viewWidth = measuredWidth
        val viewHeight = measuredHeight
        /*println("viewWidth = $viewWidth, viewHeight = $viewHeight")
        println("paddingStart = $paddingStart, paddingTop = $paddingTop")
        println("intrinsicWidth = ${visibleOff.intrinsicWidth}")*/


        val bounds = Rect(
            measuredWidth - iconVisible.intrinsicWidth,
            paddingTop,
            measuredWidth,
            measuredHeight
        )

        iconVisibleOffBounds = bounds

        iconVisible.bounds = bounds

        iconVisible.draw(canvas)
        iconVisible.draw(canvas)

    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        val newText = "************************************************************************"

        super.setText(newText, type)
    }

}