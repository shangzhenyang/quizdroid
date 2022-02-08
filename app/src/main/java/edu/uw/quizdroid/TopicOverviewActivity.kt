package edu.uw.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class TopicOverviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_overview)

        val labelTitle = findViewById<TextView>(R.id.label_title)
        val labelQuestionCount = findViewById<TextView>(R.id.label_question_count)
        val labelDescription = findViewById<TextView>(R.id.label_description)
        val icon = findViewById<ImageView>(R.id.icon_quiz_overview)
        val btnBegin = findViewById<Button>(R.id.btn_begin)

        val topic = intent.getStringExtra("topic")
        labelTitle.text = topic

        val topics = Topics()
        val topicObj = topics.getByTitle(topic)
        if (topicObj != null) {
            val questionCount = topicObj.questions.size
            if (questionCount == 1) {
                labelQuestionCount.text = getString(R.string.one_question)
            } else {
                labelQuestionCount.text = getString(R.string.multiple_questions, questionCount)
            }
            labelDescription.text = topicObj.longDescription
            icon.setImageResource(topicObj.icon)
        }

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


