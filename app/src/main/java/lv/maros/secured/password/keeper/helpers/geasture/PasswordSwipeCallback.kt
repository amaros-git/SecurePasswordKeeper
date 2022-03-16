package lv.maros.secured.password.keeper.helpers.geasture

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import lv.maros.secured.password.keeper.R

class PasswordItemSwipeCallback(
    context: Context,
    private val swipeListener: PasswordItemSwipeListener,
    dragDirs: Int = 0,
    swipeDirs: Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) : ItemTouchHelper.SimpleCallback(
    dragDirs, swipeDirs
) {
    private val deleteIcon: Drawable =
        ContextCompat.getDrawable(context, R.drawable.baseline_delete_black_48)!! //TODO

    private val editIcon: Drawable =
        ContextCompat.getDrawable(context, R.drawable.baseline_edit_black_48)!! //TODO

    private val marginMedium = context.resources.getDimension(R.dimen.margin_medium).toInt()

    private var swipedPos = -1

    private fun drawIcon(itemView: View, dX: Float, c: Canvas) {
        when {
            dX > 0 -> { //swipe right
                drawEditIcon(itemView, c)
            }
            dX < 0 -> { //swipe left
                drawDeleteIcon(itemView, c)
            }
        }
    }

    private fun drawEditIcon(itemView: View, c: Canvas) {
        val editIconRight = itemView.left + editIcon.intrinsicWidth + marginMedium
        val editIconLeft = itemView.left + marginMedium
        val editIconTop = itemView.top + (itemView.height - editIcon.intrinsicHeight) / 2
        val editIconBottom = editIconTop + editIcon.intrinsicHeight

        editIcon.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom)
        editIcon.draw(c)
    }

    private fun drawDeleteIcon(itemView: View, c: Canvas) {
        val deleteIconLeft = itemView.right - deleteIcon.intrinsicWidth - marginMedium
        val deleteIconRight = itemView.right - marginMedium
        val deleteIconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
        val deleteIconBottom = deleteIconTop + deleteIcon.intrinsicHeight

        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon.draw(c)
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
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
        drawIcon(viewHolder.itemView, dX, c)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
        swipedPos = viewHolder.adapterPosition
        when (swipeDir) {
            ItemTouchHelper.RIGHT -> swipeListener.onSwipeRight(swipedPos)
            ItemTouchHelper.LEFT -> swipeListener.onSwipeLeft(swipedPos)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

}


interface PasswordItemSwipeListener {
    fun onSwipeLeft(swipedPos: Int)

    fun onSwipeRight(swipedPos: Int)
}
