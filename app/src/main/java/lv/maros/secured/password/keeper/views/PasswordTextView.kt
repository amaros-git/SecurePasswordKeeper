package lv.maros.secured.password.keeper.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
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

    //TODO rework to attributes !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private val visibleOff: Drawable =
        ContextCompat.getDrawable(context, R.drawable.outline_visibility_off_black_18)!!

    private val visible: Drawable =
        ContextCompat.getDrawable(context, R.drawable.outline_visibility_black_18)!!

    private var widthSize = 0
    private var heightSize = 0

    init {
        isClickable = false
        isSingleLine = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val viewWidth = measuredWidth
        val viewHeight = measuredHeight
        println("viewWidth = $viewWidth, viewHeight = $viewHeight")

        val textSize = meas
        println("currentText = $currentText")


        /*canvas.drawRect(
            paddingStart.toFloat(),
            paddingTop.toFloat(),
            18.0f, height, paint
        )*/

        println("paddingStart = $paddingStart, paddingTop = $paddingTop")
        println("intrinsicWidth = ${visibleOff.intrinsicWidth}")



        visible.bounds = Rect(
            viewWidth - visibleOff.intrinsicWidth - paddingEnd,
            paddingTop,
            viewWidth - paddingEnd,
            viewHeight
        )

        visibleOff.draw(canvas)
        visible.draw(canvas)

    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        val newText = "************************************************************************"

        super.setText(newText, type)
    }

}