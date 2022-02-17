package edu.uw.quizdroid

import android.content.Context
import android.os.Environment
import org.json.JSONArray
import java.io.File
import java.io.FileReader

class Topics(context: Context) : TopicRepository {
    private val data = ArrayList<Topic>()

    init {
        val filePath = context.filesDir
        val file = File(filePath, "questions.json")
        val reader = FileReader(file)
        var txt = reader.readText()
        reader.close()

        val jsonArr = JSONArray(txt)
        for (i in 0 until jsonArr.length()) {
            val topicJson = jsonArr.getJSONObject(i)
            val title = topicJson.getString("title")
            val desc = topicJson.getString("desc")
            val questions = topicJson.getJSONArray("questions")
            val questionList = ArrayList<Quiz>()
            for (j in 0 until questions.length()) {
                val questionObj = questions.getJSONObject(j)
                val questionText = questionObj.getString("text")
                val correctAnswer = questionObj.getString("answer").toInt() - 1
                val answersJson = questionObj.getJSONArray("answers")
                val answersArr = arrayOf(
                    answersJson.getString(0),
                    answersJson.getString(1),
                    answersJson.getString(2),
                    answersJson.getString(3)
                )
                val quizObj = Quiz(questionText, answersArr, correctAnswer)
                questionList.add(quizObj)
            }
            val topicObj = Topic(title, desc, desc, questionList, android.R.drawable.ic_menu_gallery)
            data.add(topicObj)
        }
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