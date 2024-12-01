package com.test.task.convertor

import com.test.task.bom.Task
import com.test.task.bom.User
import com.test.task.dto.TaskDTO
import com.test.task.dto.UserDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserConvertor(@Autowired private val taskConvertor: TaskConvertor) {

    fun fromDTO(source: UserDTO, destination: User) {
        destination.id = source.id
        destination.username = source.username
        destination.password = source.password
        destination.isAdministrator = source.isAdministrator

        if (source.tasks.isNotEmpty()) {
            for (taskDTO in source.tasks) {
                val task = Task()
                taskConvertor.fromDTO(taskDTO, task)
                destination.tasks.add(task)
            }
        }
    }

    fun toDTO(source: User, destination: UserDTO) {
        destination.id = source.id
        destination.username = source.username
        destination.password = source.password
        destination.isAdministrator = source.isAdministrator

        if (source.tasks.isNotEmpty()) {
            for (task in source.tasks) {
                val taskDTO = TaskDTO()
                taskConvertor.toDTO(task, taskDTO)
                destination.tasks.add(taskDTO)
            }
        }
    }
}
