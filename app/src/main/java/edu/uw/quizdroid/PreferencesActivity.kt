package edu.uw.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class PreferencesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        val inputQuestionUrl = findViewById<TextInputEditText>(R.id.input_question_url)
        val inputDownloadInterval = findViewById<TextInputEditText>(R.id.input_download_interval)
        val btnSave = findViewById<Button>(R.id.btn_save)

        val sharedPref = getSharedPreferences("QuizDroid", MODE_PRIVATE)
        inputQuestionUrl.setText(sharedPref.getString("serverUrl", "http://tednewardsandbox.site44.com/questions.json"))
        inputDownloadInterval.setText(sharedPref.getString("downloadInterval", "10"))

        btnSave.setOnClickListener {
            sharedPref.edit().putString("serverUrl", inputQuestionUrl.text.toString()).apply()
            sharedPref.edit().putString("downloadInterval", inputDownloadInterval.text.toString()).apply()
            Toast.makeText(applicationContext, getString(R.string.saved), Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }
}