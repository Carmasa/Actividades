package com.example.loginsimple

data class Task(
    val id: Int,
    var name: String,
    var isCompleted: Boolean = false,
    var priority: String = "Normal" // Alta (Rojo), Normal (Naranja), Baja (Verde)
)
