package com.test.task.services

import com.test.task.bom.Status
import com.test.task.bom.Task
import com.test.task.dto.CreateTaskDTO
import com.test.task.dto.UserStatistic
import com.test.task.exceptions.AccessForbiddenException
import com.test.task.exceptions.TaskAlreadyHasStatusDoneException
import com.test.task.exceptions.TaskNotFoundByTitleException
import com.test.task.exceptions.TaskNotFoundException
import com.test.task.repositories.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TaskService(
    @Autowired private val taskRepository: TaskRepository,
    @Autowired private val userService: UserService
) {
    fun create(createTaskDTO: CreateTaskDTO, username: String): Task {
        val user = userService.getByUsername(username)
        var task = Task()
        task.title = createTaskDTO.title
        task.description = createTaskDTO.description
        task.dueDate = createTaskDTO.dueDate
        task.priority = createTaskDTO.priority
        task.user = user
        task.status = Status.NOT_DONE
        task = taskRepository.save(task)
        return task
    }

    fun changeStatus(id: Long, username: String): Task? {
        val user = userService.getByUsername(username)
        var task = taskRepository.getTaskById(id) ?: throw TaskNotFoundException()
        if (task.user.id != user.id && !user.isAdministrator) {
            throw AccessForbiddenException()
        }
        if (task.status.name == "DONE") {
            throw TaskAlreadyHasStatusDoneException(id)
        }
        task.status = Status.DONE
        task = taskRepository.save(task)
        return task
    }

    fun update(id: Long, createTaskDTO: CreateTaskDTO, username: String): Task? {
        val user = userService.getByUsername(username)
        var task = taskRepository.getTaskById(id) ?: throw TaskNotFoundException()
        if (task.user.id != user.id && !user.isAdministrator) {
            throw AccessForbiddenException()
        }
        task.title = createTaskDTO.title
        task.description = createTaskDTO.description
        task.dueDate = createTaskDTO.dueDate
        task.priority = createTaskDTO.priority
        task = taskRepository.save(task)
        return task
    }

    fun findAllByStatus(status: String, username: String): List<Task> {
        val userDTO = userService.getByUsername(username)
        val tasks: List<Task> = if (status != "") {
            taskRepository.findTaskByStatusAndUserId(Status.valueOf(status), userDTO.id)!!
        } else {
            taskRepository.findAllByUserId(userDTO.id)!!
        }
        return tasks
    }

    fun findAll(): MutableList<Task> {
        return taskRepository.findAll()
    }

    fun getById(id: Long): Task {
        return taskRepository.getReferenceById(id)
    }

    fun delete(id: Long, username: String) {
        val user = userService.getByUsername(username)
        val task = taskRepository.getTaskById(id) ?: throw TaskNotFoundByTitleException(id)
        if (task.user.id != user.id && !user.isAdministrator) {
            throw AccessForbiddenException()
        }
        taskRepository.delete(task)
    }

    fun getStatisticByStatus(status: String): List<Task>? {
        val tasks = taskRepository.findAllByStatus(Status.valueOf(status))
        return tasks
    }

    fun getStatisticByUsers(): MutableList<UserStatistic> {
        val users = userService.findAll()
        val usersStatistics: MutableList<UserStatistic> = mutableListOf()
        for (user in users) {
            val userStatistic = UserStatistic(user.username, 0)
            val countTask = taskRepository.findAllByUserId(user.id)
            if (!countTask.isNullOrEmpty()) {
                userStatistic.countTask = countTask.size
            }
            usersStatistics.add(userStatistic)
        }
        return usersStatistics
    }

}
