package edu.uw.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView

class QuestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        val labelQuestion = findViewById<TextView>(R.id.label_question)
        val optsQuestions = findViewById<RadioGroup>(R.id.opts_questions)
        val radio1 = findViewById<RadioButton>(R.id.radio1)
        val radio2 = findViewById<RadioButton>(R.id.radio2)
        val radio3 = findViewById<RadioButton>(R.id.radio3)
        val radio4 = findViewById<RadioButton>(R.id.radio4)
        val btnSubmit = findViewById<Button>(R.id.btn_submit)

        val questionIndex = intent.getIntExtra("question_index", 0)
        val topic = intent.getStringExtra("topic")

        var correctCount = intent.getIntExtra("correct_count", 0)

        val topics = Topics()
        val questionsArray = topics.getByTitle(topic)?.questions
        val question = questionsArray?.elementAt(questionIndex)
        val answers = question?.answers
        val correctAnswer = question?.answers?.elementAt(question.correctAnswer)

        var correctCountAdded = false
        var selectedAnswer = ""

        labelQuestion.text = question?.question
        radio1.text = answers?.elementAt(0)
        radio2.text = answers?.elementAt(1)
        radio3.text = answers?.elementAt(2)
        radio4.text = answers?.elementAt(3)

        optsQuestions.setOnCheckedChangeListener { _, checkedId ->
            btnSubmit.visibility = View.VISIBLE
            val radio = findViewById<RadioButton>(checkedId)
            selectedAnswer = radio.text.toString()
        }

        btnSubmit.setOnClickListener {
            if (!correctCountAdded && selectedAnswer == correctAnswer) {
                correctCount++
                correctCountAdded = true
            }
            startActivity(
                Intent(this, AnswerActivity::class.java).apply {
                    putExtra("correct_answer", correctAnswer)
                    putExtra("correct_count", correctCount)
                    putExtra("question_index", questionIndex)
                    putExtra("selected_answer", selectedAnswer)
                    putExtra("topic", topic)
                    putExtra("total_count", questionsArray?.size)
                }
            )
        }
    }
}
