package com.test.task.dto

import com.test.task.bom.Priority
import com.test.task.bom.Status
import com.test.task.bom.User
import java.util.*

data class TaskDTO(
    var id: Long,
    var title: String,
    var description: String,
    var dueDate: Date,
    var priority: Priority,
    var status: Status,
){
    constructor() : this(0, "", "", Date(), Priority.LOW, Status.NOT_DONE)
}
