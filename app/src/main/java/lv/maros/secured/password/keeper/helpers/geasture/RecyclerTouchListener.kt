package lv.maros.keeper.helpers.geasture

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import lv.maros.keeper.utils.ClickListener


class RecyclerTouchListener(
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val clickListener: ClickListener
) : RecyclerView.OnItemTouchListener {

    private val gestureDetector: GestureDetector = GestureDetector(
        context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                super.onLongPress(e)

                val child = recyclerView.findChildViewUnder(e.x, e.y)
                child?.let {
                    clickListener.onLongClick(it, recyclerView.getChildAdapterPosition(child))
                }
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                return true
            }
        }
    )


    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)

        if (null != child && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, recyclerView.getChildAdapterPosition(child))
        }

        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }
}