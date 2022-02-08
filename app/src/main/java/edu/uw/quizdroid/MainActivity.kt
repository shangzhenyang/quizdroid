package edu.uw.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listTopic = findViewById<ListView>(R.id.list_topic)
        val topics = Topics().getAll()
        val listItems = ArrayList<Map<String, Any>>()
        for (item in topics) {
            val map = HashMap<String, Any>()
            map["title"] = item.title
            map["description"] = item.shortDescription
            map["icon"] = item.icon
            listItems.add(map)
        }
        val from = arrayOf("title", "description", "icon")
        val to = arrayOf(
            R.id.label_quiz_title,
            R.id.label_quiz_description,
            R.id.icon_quiz
        ).toIntArray()
        listTopic.adapter = SimpleAdapter(this, listItems, R.layout.list_item, from, to)
        listTopic.setOnItemClickListener {_, _, position, _ ->
            startActivity(
                Intent(this, TopicOverviewActivity::class.java).apply {
                    putExtra("topic", topics.elementAt(position).title)
                }
            )
        }
    }
}