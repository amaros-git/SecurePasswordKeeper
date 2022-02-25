package lv.maros.secured.password.keeper.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import timber.log.Timber

class PasswordItemCopyButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): AppCompatImageButton(context, attrs), View.OnClickListener {

    val position = -1 //holder view position in recycler view

    private var copyOnClickListener: OnCopyClickListener? = null

    init {
        setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (position >= 0) {
            copyOnClickListener?.invoke(v, position)
        } else {
            Timber.e("Wrong position was set: $position")
        }
    }

    fun setOnCopyClickListener(clickListener: OnCopyClickListener) {
        copyOnClickListener = clickListener
    }
}

/**
 * int is a position
 */
typealias OnCopyClickListener = (View, Int) -> String