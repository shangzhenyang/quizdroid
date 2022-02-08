package edu.uw.quizdroid

class Topics : TopicRepository {
    private val data = arrayListOf<Topic>(
        Topic(
            "Math",
            "A math quiz.",
            "This is a math quiz.",
            arrayListOf<Quiz>(
                Quiz(
                    "1 + 1 = ?",
                    arrayOf(
                        "11",
                        "2",
                        "3",
                        "None of the above"
                    ),
                    1
                ),
                Quiz(
                    "2 * 3 = ?",
                    arrayOf(
                        "6",
                        "5",
                        "3",
                        "None of the above"
                    ),
                    0
                ),
                Quiz(
                    "sin(2 / pi) * log(10) = ?",
                    arrayOf(
                        "0.5944807685",
                        "6.2831853072",
                        "12",
                        "None of the above"
                    ),
                    0
                )
            ),
            android.R.drawable.ic_menu_week
        ),
        Topic(
            "Physics",
            "A physics quiz.",
            "This is a physics quiz.",
            arrayListOf<Quiz>(
                Quiz(
                    "What does F = ma stand for?",
                    arrayOf(
                        "momentum = mass * velocity",
                        "force = mass * acceleration",
                        "power = energy transferred time taken",
                        "None of the above"
                    ),
                    1
                ),
                Quiz(
                    "What does p = mv stand for?",
                    arrayOf(
                        "force = mass * acceleration",
                        "momentum = mass * velocity",
                        "power = energy transferred time taken",
                        "None of the above"
                    ),
                    2
                )
            ),
            android.R.drawable.ic_menu_compass
        ),
        Topic(
            "Marvel Super Heroes",
            "A quiz of Marvel super heroes.",
            "This is a quiz of Marvel super heroes.",
            arrayListOf<Quiz>(
                Quiz(
                    "Which of the following is not a Marvel super hero?",
                    arrayOf(
                        "Spider Man",
                        "Iron Man",
                        "Vision",
                        "Peppa Pig"
                    ),
                    3
                )
            ),
            android.R.drawable.ic_menu_mapmode
        )
    )

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