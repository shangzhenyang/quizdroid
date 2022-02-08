package edu.uw.quizdroid

class Topic(
    val title: String,
    val shortDescription: String,
    val longDescription: String,
    val questions: Collection<Quiz>,
    val icon: Int
)