package yamoney.ru.customlayoutmanager

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet

const val CARD_PROPORTION = 1.57f

class SampleCardView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : CardView(context, attrs, defStyle) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = (width / CARD_PROPORTION).toInt()
        setMeasuredDimension(width, height)
    }
}