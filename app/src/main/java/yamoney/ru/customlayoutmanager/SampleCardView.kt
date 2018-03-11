package yamoney.ru.customlayoutmanager

import android.content.Context
import android.support.v4.widget.TextViewCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.TextView

const val CARD_PROPORTION = 1.57f

class SampleCardView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : CardView(context, attrs, defStyle) {

    private val textView = TextView(context).apply {
        TextViewCompat.setTextAppearance(this, android.R.style.TextAppearance_Large)
    }

    init {
        val lp = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        }
        addView(textView, lp)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = (width / CARD_PROPORTION).toInt()
        setMeasuredDimension(width, height)
    }

    fun setTitle(title: CharSequence) {
        textView.text = title
    }

}
