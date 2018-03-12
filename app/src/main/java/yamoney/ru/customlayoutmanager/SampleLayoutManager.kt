package yamoney.ru.customlayoutmanager

import android.graphics.PointF
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.LayoutParams
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT

class SampleLayoutManager : RecyclerView.LayoutManager(), RecyclerView.SmoothScroller.ScrollVectorProvider {

    private var gap = 0
    private var scrollOffset = 0

    override fun generateDefaultLayoutParams() = LayoutParams(MATCH_PARENT, WRAP_CONTENT)

    override fun canScrollVertically() = true

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (width == 0) {
            return
        }

        if (state.itemCount == 0) {
            removeAllViews()
            return
        }

        if (childCount == 0 && state.isPreLayout) {
            return
        }

        calculateGaps(state)
        detachAndScrapAttachedViews(recycler)
        visibleRange(state).forEach { addMeasureAndLayoutChild(recycler, it) }
    }

    private fun calculateGaps(state: RecyclerView.State) {
        val minGap = (cardHeight() * 0.38f).toInt()
        gap = (visibleHeight() / state.itemCount).coerceAtLeast(minGap)
        fixScrollOffset(state)
    }

    private fun fixScrollOffset(state: RecyclerView.State) {
        scrollOffset = scrollOffset
                .coerceAtMost((state.itemCount - 1) * gap + cardHeight() - visibleHeight())
                .coerceAtLeast(0)
    }

    private fun cardHeight() = (cardWidth() / CARD_PROPORTION).toInt()

    private fun cardWidth() = width - paddingLeft - paddingRight

    private fun visibleHeight() = height - paddingTop - paddingBottom

    private fun visibleRange(state: RecyclerView.State): IntProgression {
        val start = scrollOffset / gap
        val end = start + (height / gap) + 1
        return IntProgression.fromClosedRange(start.coerceIn(0, state.itemCount - 1),
                end.coerceIn(0, state.itemCount - 1), 1)
    }

    private fun addMeasureAndLayoutChild(recycler: RecyclerView.Recycler, position: Int): View {
        val view = recycler.getViewForPosition(position)
        val y = gap * position - scrollOffset + paddingTop
        addView(view)
        measureChild(view, 0, 0)
        layoutDecorated(view, paddingLeft, y, width - paddingRight, y + view.measuredHeight)
        return view
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        val currentScrollOffset = scrollOffset
        scrollOffset += dy
        fixScrollOffset(state)
        onLayoutChildren(recycler, state)
        return scrollOffset - currentScrollOffset
    }

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF {
        val y = gap * targetPosition - scrollOffset
        return when {
            y < paddingTop -> PointF(0f, -1f)
            y > height - paddingBottom -> PointF(0f, 1f)
            else -> PointF(0f, 0f)
        }
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State, position: Int) {
        val smoothScroller = LinearSmoothScroller(recyclerView.context).apply {
            targetPosition = position
        }
        startSmoothScroll(smoothScroller)
    }
}
