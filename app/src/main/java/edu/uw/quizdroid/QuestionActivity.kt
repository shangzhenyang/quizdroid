package edu.uw.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject

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

        val questions = """
        {
            "Math": [
                {
                    "question": "1 + 1 = ?",
                    "options": [
                        "11",
                        "2",
                        "3",
                        "None of the above"
                    ],
                    "correct": 1
                },
                {
                    "question": "2 * 3 = ?",
                    "options": [
                        "6",
                        "5",
                        "3",
                        "None of the above"
                    ],
                    "correct": 0
                },
                {
                    "question": "sin(2 / pi) * log(10) = ?",
                    "options": [
                        "0.5944807685",
                        "6.2831853072",
                        "12",
                        "None of the above"
                    ],
                    "correct": 0
                }
            ],
            "Physics": [
                {
                    "question": "What does F = ma stand for?",
                    "options": [
                        "momentum = mass * velocity",
                        "force = mass * acceleration",
                        "power = energy transferred time taken",
                        "None of the above"
                    ],
                    "correct": 1
                },
                {
                    "question": "What does p = mv stand for?",
                    "options": [
                        "force = mass * acceleration",
                        "momentum = mass * velocity",
                        "power = energy transferred time taken",
                        "None of the above"
                    ],
                    "correct": 2
                }
            ],
            "Marvel Super Heroes": [
                {
                    "question": "Which of the following is not a Marvel super hero?",
                    "options": [
                        "Spider Man",
                        "Iron Man",
                        "Vision",
                        "Peppa Pig"
                    ],
                    "correct": 3
                }
            ],
            "Spanish": [
                {
                    "question": "What is \"hello\" in Spanish?",
                    "options": ["Hola", "Buenos días", "Cómo estás", "Adiós"],
                    "correct": 0
                }
            ]
        }
        """

        val questionObj = JSONObject(questions)
        val questionsArray = questionObj.getJSONArray(topic)
        val question = questionsArray.getJSONObject(questionIndex)
        val options = question.getJSONArray("options")
        val correctAnswer = options.getString(question.getInt("correct"))

        var correctCountAdded = false
        var selectedAnswer = ""

        labelQuestion.text = question.getString("question")
        radio1.text = options.getString(0)
        radio2.text = options.getString(1)
        radio3.text = options.getString(2)
        radio4.text = options.getString(3)

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
                    putExtra("total_count", questionsArray.length())
                }
            )
        }
    }
}