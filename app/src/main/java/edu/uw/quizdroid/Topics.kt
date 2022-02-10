package edu.uw.quizdroid

import org.json.JSONArray
import org.json.JSONObject

class Topics : TopicRepository {
    private val data = ArrayList<Topic>()

    init {
//        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        val reader = FileReader("$dir/questions.json")
//        val txt = reader.readText()
//        reader.close()
//        Log.i("QuizDroid",txt)

        val txt = """
            [
                { "title":"Science!",
                  "desc":"Because SCIENCE!",
                  "questions":[
                    {
                      "text":"What is fire?",
                      "answer":"1",
                      "answers":[
                        "One of the four classical elements",
                        "A magical reaction given to us by God",
                        "A band that hasn't yet been discovered",
                        "Fire! Fire! Fire! heh-heh"
                      ]
                    }
                  ]
                },
                { "title":"Marvel Super Heroes", "desc": "Avengers, Assemble!",
                  "questions":[
                    {
                      "text":"Who is Iron Man?",
                      "answer":"1",
                      "answers":[
                        "Tony Stark",
                        "Obadiah Stane",
                        "A rock hit by Megadeth",
                        "Nobody knows"
                      ]
                    },
                    {
                      "text":"Who founded the X-Men?",
                      "answer":"2",
                      "answers":[
                        "Tony Stark",
                        "Professor X",
                        "The X-Institute",
                        "Erik Lensherr"
                      ]
                    },
                    {
                      "text":"How did Spider-Man get his powers?",
                      "answer":"1",
                      "answers":[
                        "He was bitten by a radioactive spider",
                        "He ate a radioactive spider",
                        "He is a radioactive spider",
                        "He looked at a radioactive spider"
                      ]
                    }
                  ]
                },
                { "title":"Mathematics", "desc":"Did you pass the third grade?",
                  "questions":[
                     {
                       "text":"What is 2+2?",
                       "answer":"1",
                       "answers":[
                         "4",
                         "22",
                         "An irrational number",
                         "Nobody knows"
                       ]
                     }
                  ]
               }
            ]
        """
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