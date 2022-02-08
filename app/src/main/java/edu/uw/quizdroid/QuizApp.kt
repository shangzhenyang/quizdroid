package edu.uw.quizdroid

import android.app.Application
import android.util.Log

class QuizApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i("QuizApp","being loaded and run")
    }
}