package edu.uw.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listTopic = findViewById<ListView>(R.id.list_topic)
        val listItems = arrayOf("Math", "Physics", "Marvel Super Heroes", "Spanish")
        listTopic.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        listTopic.setOnItemClickListener {_, _, position, _ ->
            startActivity(
                Intent(this, TopicOverviewActivity::class.java).apply {
                    putExtra("topic", listItems[position])
                }
            )
        }
    }
}