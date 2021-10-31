package lv.maros.keeper.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import android.graphics.drawable.Drawable
import android.view.MotionEvent

import android.view.View
import androidx.core.content.ContextCompat
import lv.maros.keeper.R
import timber.log.Timber


class PasswordItemSwipeCallback(context: Context, swipeDirs: Int, dragDirs: Int = 0) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    private val background = ColorDrawable(Color.BLUE)

    private val deleteIcon: Drawable =
        ContextCompat.getDrawable(context, R.drawable.ic_delete_black_48dp)!!

    private val marginMedium = context.resources.getDimension(R.dimen.margin_medium).toInt()

    private val swipeDxLimit = 6 //TODO should depend on screen size

    init {
        Timber.d("intrinsicHeight = ${deleteIcon.intrinsicHeight}, intrinsicWidth = ${deleteIcon.intrinsicWidth} ")

    }

    /**
     * this method is called when swipe is finished. I use it to
     * bind
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
        val position = viewHolder.adapterPosition
        val itemView = viewHolder.itemView
        Timber.d("performed swipe on $position")
        
        itemView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Timber.d("down")
                }
                MotionEvent.ACTION_UP -> {
                    Timber.d("Click")
                    v.performClick()
                }
            }
            false
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX / swipeDxLimit,
            dY,
            actionState,
            isCurrentlyActive
        )

        drawIcon(viewHolder.itemView, dX, c)

    }

    private fun drawIcon(itemView: View, dX: Float, c: Canvas) {

        val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
        val iconBottom = iconTop + deleteIcon.intrinsicHeight

        when {
            dX > 0 -> { //swipe right
                /*val iconLeft = itemView.left + iconMargin + deleteIcon.intrinsicWidth
                val iconRight = itemView.left + iconMargin
                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)*/
            }
            dX < 0 -> {
                val iconLeft = itemView.right - deleteIcon.intrinsicWidth - marginMedium
                val iconRight = itemView.right - marginMedium
                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            }
            else -> {
                deleteIcon.setBounds(0, 0, 0, 0)
            }
        }

        deleteIcon.draw(c)
    }

    private fun drawBackground(itemView: View, dX: Float, c: Canvas) {
        when {
            dX > 0 -> { //swipe right
                background.setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.left + dX.toInt(),
                    itemView.bottom
                )
            }
            dX < 0 -> {
                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
            }
            else -> {
                background.setBounds(0, 0, 0, 0)
            }
        }

        background.draw(c)
    }


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

}