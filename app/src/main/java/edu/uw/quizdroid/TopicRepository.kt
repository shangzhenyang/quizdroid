package edu.uw.quizdroid

interface TopicRepository {
    fun get(id: Int): Topic
    fun getAll(): Iterable<Topic>
    fun getByTitle(title: String?): Topic?
    fun add(newValue: Topic)
    fun change(id: Int, newValue: Topic): Boolean
    fun remove(id: Int): Boolean
}