package edu.uw.quizdroid

import android.os.Environment
import android.util.Log
import java.io.FileReader

class Topics : TopicRepository {
    private val data = ArrayList<Topic>()

    init {
        val dir = Environment.getDataDirectory().toString()
        val reader = FileReader("$dir/questions.json")
        val txt = reader.readText()
        reader.close()
        Log.i("QuizDroid",txt)
    }

    override fun get(id: Int): Topic {
        return data[id]
    }

    override fun getAll(): Iterable<Topic> {
        return data
    }

    override fun getByTitle(title: String?): Topic? {
        if (title == null) {
            return null
        }
        for (item in data) {
            if (item.title == title) {
                return item
            }
        }
        return null
    }

    override fun add(newValue: Topic) {
        data.add(newValue)
    }

    override fun change(id: Int, newValue: Topic): Boolean {
        if (data[id] == null) {
            return false
        }
        data[id] = newValue
        return true
    }

    override fun remove(id: Int): Boolean {
        if (data[id] == null) {
            return false
        }
        data.removeAt(id)
        return true
    }
}