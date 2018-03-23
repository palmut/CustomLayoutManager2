package yamoney.ru.customlayoutmanager

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.text.TextPaint
import android.view.View

interface StickyHeaderAdapter {
    fun getHeader(position: Int): String
}

class StickyHeadersDecoration(
        private val headerHeight: Int,
        fgColor: Int,
        bgColor: Int,
        headerTextSize: Float,
        private val headerAdapter: StickyHeaderAdapter
) : RecyclerView.ItemDecoration() {

    private val fgPaint = TextPaint().apply {
        color = fgColor
        isAntiAlias = true
        this.textSize = headerTextSize
    }
    private val bgPaint = Paint().apply { color = bgColor }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val adapterPosition = parent.getChildAdapterPosition(view)
        val topOffset = if (hasHeader(adapterPosition)) headerHeight else 0
        outRect.set(0, topOffset, 0, 0)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        var headerTop = parent.height.toFloat()
        val bounds = Rect()
        (parent.childCount - 1 downTo 0).forEach {
            val child = parent.getChildAt(it)
            val adapterPosition = parent.getChildAdapterPosition(child)
            if (it == 0 || hasHeader(adapterPosition)) {
                headerTop = if (it > 0) {
                    (child.y - headerHeight)
                } else {
                    (headerTop - headerHeight).coerceAtMost(0f)
                }
                c.drawRect(child.x, headerTop, parent.right.toFloat(), headerTop + headerHeight, bgPaint)
                val text = headerAdapter.getHeader(adapterPosition)
                fgPaint.getTextBounds(text, 0, text.length, bounds)
                val textPadding = (headerHeight - bounds.height()) / 2
                val textTop = headerTop + textPadding + bounds.height()
                c.drawText(text, 0, text.length, child.x + textPadding, textTop, fgPaint)
            }
        }
    }

    private fun hasHeader(position: Int) =
            position == 0 || getHeaderId(position) != getHeaderId(position - 1)

    private fun getHeaderId(position: Int) = headerAdapter.getHeader(position).hashCode()
}