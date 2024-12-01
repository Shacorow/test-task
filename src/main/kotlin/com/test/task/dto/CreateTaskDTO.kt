package com.test.task.dto

import com.test.task.bom.Priority
import java.util.*

data class CreateTaskDTO(
    val title: String,
    val description: String,
    val dueDate: Date,
    val priority: Priority
)
