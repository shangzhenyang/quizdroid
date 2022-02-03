package edu.uw.quizdroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AnswerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        val labelAnsSelected = findViewById<TextView>(R.id.label_ans_selected)
        val labelAnsCorrect = findViewById<TextView>(R.id.label_ans_correct)
        val labelCorrectVsTotal = findViewById<TextView>(R.id.label_correct_vs_total)
        val btnNext = findViewById<Button>(R.id.btn_next)

        val correctAnswer = intent.getStringExtra("correct_answer")
        val correctCount = intent.getIntExtra("correct_count", 0)
        val questionIndex = intent.getIntExtra("question_index", 0)
        val selectedAnswer = intent.getStringExtra("selected_answer")
        val topic = intent.getStringExtra("topic")
        val totalCount = intent.getIntExtra("total_count", 0)

        labelAnsSelected.text = getString(R.string.selected_answer, selectedAnswer)
        labelAnsCorrect.text = getString(R.string.correct_answer, correctAnswer)
        labelCorrectVsTotal.text = getString(R.string.correct_vs_total, correctCount, totalCount)

        if (questionIndex + 1 >= totalCount) {
            btnNext.text = getString(R.string.finish)
            btnNext.setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
        } else {
            btnNext.setOnClickListener {
                startActivity(Intent(this, QuestionActivity::class.java).apply {
                    putExtra("correct_count", correctCount)
                    putExtra("question_index", questionIndex + 1)
                    putExtra("topic", topic)
                })
            }
        }
    }
}