package lv.maros.secured.password.keeper.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import timber.log.Timber

class PasswordItemCopyButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): AppCompatImageButton(context, attrs), View.OnClickListener {

    private var position = -1

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

    fun setOnCopyClickListener(position: Int, clickListener: OnCopyClickListener) {
        this.position = position
        copyOnClickListener = clickListener
    }
}

/**
 * Int is a view holder position in recycler view
 */
typealias OnCopyClickListener = (View, Int) -> Unit