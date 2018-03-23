package yamoney.ru.customlayoutmanager

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_sample_list.recyclerView
import kotlinx.android.synthetic.main.activity_sample_list.toolbar

class SampleListActivity : AppCompatActivity() {

    private val listAdapter = ListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_list)

        setSupportActionBar(toolbar)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                    SeparatorDecoration(
                            resources.getDimension(R.dimen.left_indent),
                            resources.getDimension(R.dimen.separator_height),
                            ResourcesCompat.getColor(resources, R.color.colorPrimary, theme)
                    )
            )
            addItemDecoration(
                    StickyHeadersDecoration(
                            resources.getDimensionPixelSize(R.dimen.headerHeight),
                            Color.WHITE,
                            ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, theme),
                            resources.getDimension(R.dimen.headerTextSize),
                            listAdapter
                    )
            )
            adapter = listAdapter
        }
    }
}

class ListItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

class ListAdapter : RecyclerView.Adapter<ListItemViewHolder>(), StickyHeaderAdapter {

    private val items = Array(100) { "Item ${it + 1}" }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ListItemViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.item, parent, false)
            )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        (holder.itemView as TextView).text = items[position]
    }

    override fun getHeader(position: Int) = "Header ${position / 4}"

}
