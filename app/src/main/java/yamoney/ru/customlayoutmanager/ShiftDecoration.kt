package yamoney.ru.customlayoutmanager

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class ShiftDecoration(private val shift: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val group = parent.getChildAdapterPosition(view) / 2
        val left = shift * (group % 2)
        val right = shift * ((group + 1) % 2)
        outRect.set(left, 0, right, 0)
    }
}