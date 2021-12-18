package lv.maros.keeper.helpers.geasture

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import android.graphics.drawable.Drawable
import android.view.GestureDetector
import android.view.MotionEvent

import android.view.View
import androidx.core.content.ContextCompat
import lv.maros.keeper.R
import timber.log.Timber

class PasswordItemSwipeCallback(
    private val context: Context,
    private val swipeDirs: Int,
    private val recyclerView: RecyclerView,
    private val dragDirs: Int = 0) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs
) {

    private lateinit var gestureDetector: GestureDetector

    private val background = ColorDrawable(Color.BLUE)

    private val deleteIcon: Drawable =
        ContextCompat.getDrawable(context, R.drawable.baseline_delete_black_48)!!

    private val editIcon: Drawable =
        ContextCompat.getDrawable(context, R.drawable.baseline_edit_black_48)!!

    private val marginMedium = context.resources.getDimension(R.dimen.margin_medium).toInt()

    private var swipeThreshold = SWIPE_IN_THRESHOLD //TODO change swiping left or right

    private var swipedPos = -1

    private var clickRegion: RectF? = null

    private val dXDivider = 6

    private val gestureListener: GestureDetector.SimpleOnGestureListener  =
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                clickRegion?.let {
                    if (it.contains(e.x, e.y)) {
                        Timber.d("onSingleTapConfirmed")
                        return true;
                    }
                }

                return false
            }
        }

    private val onTouchListener:View.OnTouchListener =
        View.OnTouchListener { v, e ->
            v.performClick()
            processOnTouch(v, e)
        }



    init {
        configureSwipeCallback()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun configureSwipeCallback() {
        gestureDetector = GestureDetector(context, gestureListener)
        recyclerView.setOnTouchListener(onTouchListener)
    }


    private fun processOnTouch(view: View, e: MotionEvent): Boolean {
        if (swipedPos >= 0) {
            val point = Point(e.rawX.toInt(), e.rawY.toInt())

            recyclerView.findViewHolderForAdapterPosition(swipedPos)?.let { viewHolder ->
                val swipedItem = viewHolder.itemView

                val rect = Rect()
                swipedItem.getGlobalVisibleRect(rect)

                return if (e.action == MotionEvent.ACTION_DOWN ||
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
        }

        swipedPos = -1

        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
        swipeThreshold = SWIPE_OUT_THRESHOLD

        Timber.d("onSwiped")

        swipedPos = viewHolder.adapterPosition
/*
        val itemView = viewHolder.itemView

        swipeThreshold = 0.5f * 10 * SwipeHelper.BUTTON_WIDTH*/

        //swipeThreshold = 0.5f * buttons.size * SwipeHelper.BUTTON_WIDTH
        /*(viewHolder as PasswordViewHolder).container.setOnClickListener {
            Timber.d("Click")
        }*/
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
            dX / dXDivider,
            dY,
            actionState,
            isCurrentlyActive
        )

        drawIcon(viewHolder.itemView, dX, c)

    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.5f * defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f * defaultValue
    }

    //private fun drawImageView()

    private fun drawIcon(itemView: View, dX: Float, c: Canvas) {
        when {
            dX > 0 -> { //swipe right
                val editIconRight = itemView.left + editIcon.intrinsicWidth + marginMedium
                val editIconLeft = itemView.left + marginMedium
                val editIconTop = itemView.top + (itemView.height - editIcon.intrinsicHeight) / 2
                val editIconBottom = editIconTop + editIcon.intrinsicHeight

                editIcon.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom)

                clickRegion = RectF(
                    editIconLeft.toFloat(),
                    editIconTop.toFloat(),
                    editIconRight.toFloat(),
                    editIconBottom.toFloat()
                )
            }
            dX < 0 -> { //swipe left
                val deleteIconLeft = itemView.right - deleteIcon.intrinsicWidth - marginMedium
                val deleteIconRight = itemView.right - marginMedium
                val deleteIconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
                val deleteIconBottom = deleteIconTop + deleteIcon.intrinsicHeight

                deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)

                clickRegion = RectF(
                    deleteIconLeft.toFloat(),
                    deleteIconTop.toFloat(),
                    deleteIconRight.toFloat(),
                    deleteIconBottom.toFloat()
                )
            }
        }

        deleteIcon.draw(c)
        editIcon.draw(c)

    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }


    companion object {
        private const val SWIPE_IN_THRESHOLD = 0.2f
        private const val SWIPE_OUT_THRESHOLD = 0.8f
    }

}

interface UnderlayButtonClickListener {
    fun onClick(pos: Int)
}