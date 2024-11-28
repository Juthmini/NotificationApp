package com.example.sample

import java.io.Serializable

data class Task(
    val title: String,
    val description: String,
    val dueDate: String,
    val priority: String
) : Serializable
