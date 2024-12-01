package com.test.task.convertor

import com.test.task.bom.Task
import com.test.task.dto.TaskDTO
import org.springframework.stereotype.Component

@Component
class TaskConvertor {
    fun fromDTO(source: TaskDTO, destination: Task) {
        destination.id = source.id
        destination.title = source.title
        destination.dueDate = source.dueDate
        destination.description = source.description
        destination.priority = source.priority
        destination.status = source.status
    }

    fun toDTO(source: Task, destination: TaskDTO) {
        destination.id = source.id
        destination.title = source.title
        destination.dueDate = source.dueDate
        destination.description = source.description
        destination.priority = source.priority
        destination.status = source.status
    }
}
