package lv.maros.secured.password.keeper.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import timber.log.Timber

class PasswordSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): AppCompatTextView(context, attrs), View.OnClickListener, View.OnLongClickListener {

    private var position = -1

    private var clickListener: OnPasswordSearchClickListener? = null

    init {
        setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (position >= 0) {
            clickListener?.invoke(position)
        } else {
            Timber.e("Wrong position was set: $position")
        }
    }

    fun setOnSearchItemClickListener(position: Int, clickListener: OnPasswordSearchClickListener) {
        this.position = position
        this.clickListener = clickListener
    }

    override fun onLongClick(v: View?): Boolean {
        return if (position >= 0) {
            clickListener?.invoke(position)
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
typealias OnPasswordSearchClickListener = (Int) -> Unit