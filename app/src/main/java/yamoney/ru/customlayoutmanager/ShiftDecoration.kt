package yamoney.ru.customlayoutmanager

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View

class ShiftDecoration(val shift: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val left = shift * (position % 2)
        val right = shift * ((position + 1) % 2)
        outRect.set(left, 0, right, 0)
    }
}