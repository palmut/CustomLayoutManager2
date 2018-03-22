package yamoney.ru.customlayoutmanager

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class SeparatorDecoration(
        private val indent: Float,
        private val separatorHeight: Float,
        color: Int
) : RecyclerView.ItemDecoration() {

    private val paint = Paint().apply { this.color = color }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutPosition = parent.getChildLayoutPosition(view)
        val offset = if (layoutPosition > 0) separatorHeight.toInt() else 0
        outRect.set(0, offset, 0, 0)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        (0 until parent.childCount).forEach {
            val child = parent.getChildAt(it)
            if (parent.getChildAdapterPosition(child) > 0) {
                c.drawRect(indent, child.y - separatorHeight, child.right.toFloat(), child.y, paint)
            }
        }
    }
}