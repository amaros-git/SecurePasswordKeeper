package lv.maros.secured.password.keeper.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import timber.log.Timber

class PasswordSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): AppCompatTextView(context, attrs), View.OnClickListener {

    private var position = -1

    private var clickListener: OnSearchItemClickListener? = null

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

    fun setOnSearchItemClickListener(position: Int, clickListener: OnSearchItemClickListener) {
        this.position = position
        this.clickListener = clickListener
    }
}

/**
 * Int is a view holder position in recycler view
 */

typealias OnSearchItemClickListener = (Int) -> Unit