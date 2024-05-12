package lv.maros.secured.password.keeper.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import timber.log.Timber

class PasswordInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): AppCompatTextView(context, attrs), View.OnClickListener, View.OnLongClickListener {

    private var position = -1

    private var clickListener: OnPasswordInfoClickListener? = null

    init {
        //setOnClickListener(this)
        setOnLongClickListener(this)

    }

    override fun onClick(v: View) {
        if (position >= 0) {
            clickListener?.invoke(v, position)
        } else {
            Timber.e("Wrong position was set: $position")
        }
    }

    fun setTouchEventListener(position: Int, touchListener: OnPasswordInfoTouchListener) {
        this.position = position
        this.clickListener = clickListener
    }

    fun setClickListener(position: Int, clickListener: OnPasswordInfoClickListener) {
        this.position = position
        this.clickListener = clickListener
    }

    override fun onLongClick(v: View): Boolean {
        return if (position >= 0) {
            clickListener?.invoke(v, position)
            true
        } else {
            Timber.e("Wrong position was set: $position")
            false
        }
    }
}

/**
 * Int is a view holder position in recycler view
 */
typealias OnPasswordInfoClickListener = (View, Int) -> Unit

typealias OnPasswordInfoTouchListener = (View, Int) -> Unit