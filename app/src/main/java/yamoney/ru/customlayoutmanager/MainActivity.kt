package yamoney.ru.customlayoutmanager

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val cardAdapter = CardAdapter()
    private val items: MutableList<Card> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fabAdd.setOnClickListener {
            generateCards(1)
            cardAdapter.items = items
            recyclerView.smoothScrollToPosition(items.size - 1)
        }

        fabShuffle.setOnClickListener {
            items.shuffle()
            cardAdapter.items = items
        }

        with(recyclerView) {
            itemAnimator = SampleItemAnimator()
            adapter = cardAdapter
            layoutManager = LinearLayoutManager(context)
        }

    }

    private fun generateCards(count: Int) {
        val rand = Random(System.currentTimeMillis())
        (0 until count).forEach {
            val alpha = 0xff
            val red = rand.nextInt(255)
            val green = rand.nextInt(255)
            val blue = rand.nextInt(255)
            val color = Color.argb(alpha, red, green, blue)
            items.add(Card(items.size, color))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
