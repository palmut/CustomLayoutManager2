package yamoney.ru.customlayoutmanager

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT

class CardAdapter : RecyclerView.Adapter<CardViewHolder>() {

    var items: MutableList<Card> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val cardView = SampleCardView(parent.context).apply {
            radius = resources.getDimension(R.dimen.card_radius)
            val margin = resources.getDimensionPixelSize(R.dimen.padding)
            layoutParams = ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(margin, margin, margin, margin)
            }
        }
        return CardViewHolder(cardView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = items[position]
        (holder.itemView as CardView).apply {
            setCardBackgroundColor(card.color)
        }
    }

}