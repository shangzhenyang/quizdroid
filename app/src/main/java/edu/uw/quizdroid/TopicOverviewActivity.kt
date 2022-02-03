package edu.uw.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.json.JSONObject

class TopicOverviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_overview)

        val labelTitle = findViewById<TextView>(R.id.label_title)
        val labelQuestionCount = findViewById<TextView>(R.id.label_question_count)
        val labelDescription = findViewById<TextView>(R.id.label_description)
        val btnBegin = findViewById<Button>(R.id.btn_begin)

        val topic = intent.getStringExtra("topic")
        labelTitle.text = topic

        val descriptions = """
        {
            "Math": {
                "count": 3,
                "description": "It is a math quiz."
            },
            "Physics": {
                "count": 2,
                "description": "It is a physics quiz."
            },
            "Marvel Super Heroes": {
                "count": 1,
                "description": "It is a quiz about Marvel super heroes."
            },
            "Spanish": {
                "count": 1,
                "description": "It is a Spanish language quiz."
            }
        }
        """

        val descObj = JSONObject(descriptions)
        val descTopicObj = descObj.getJSONObject(topic)
        val questionCount = descTopicObj.getInt("count")
        if (questionCount > 1) {
            labelQuestionCount.text = "There are $questionCount questions."
        } else {
            labelQuestionCount.text = "There is $questionCount question."
        }
        labelDescription.text = descTopicObj.getString("description")

        btnBegin.setOnClickListener {
            startActivity(
                Intent(this, QuestionActivity::class.java).apply {
                    putExtra("correct_count", 0)
                    putExtra("question_index", 0)
                    putExtra("topic", topic)
                }
            )
        }
    }
}


