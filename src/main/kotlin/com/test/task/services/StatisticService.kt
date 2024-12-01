package com.test.task.services

import com.test.task.bom.Status
import com.test.task.bom.Task
import com.test.task.dto.CreateTaskDTO
import com.test.task.dto.UserStatistic
import com.test.task.exceptions.*
import com.test.task.repositories.TaskRepository
import liquibase.util.csv.CSVWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileWriter

@Service
class StatisticService(
    @Autowired private val taskRepository: TaskRepository,
    @Autowired private val userService: UserService
) {
    fun exportUsersStatisticsToCSV(): ResponseEntity<FileSystemResource> {
        val tempFile = File.createTempFile("users_statistics", ".csv")
        val writer = CSVWriter(FileWriter(tempFile))

        writer.writeNext(arrayOf("Username", "Count Done Tasks", "Count Not Done Tasks", "All Tasks"))
        val users = userService.findAll()
        for (user in users) {
            val tasks = taskRepository.findAllByUserId(user.id)
            if (tasks != null) {
                val countDone = tasks.count { it.status == Status.DONE }
                val countNotDone = tasks.count { it.status == Status.NOT_DONE }
                val allTasksCount = tasks.size
                writer.writeNext(
                    arrayOf(
                        user.username,
                        countDone.toString(),
                        countNotDone.toString(),
                        allTasksCount.toString()
                    )
                )
            } else {
                writer.writeNext(arrayOf(user.username))
            }
        }
        writer.close()

        val headers = org.springframework.http.HttpHeaders().apply {
            add(
                org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=tasks_and_statistics.csv"
            )
            add(org.springframework.http.HttpHeaders.CONTENT_TYPE, "text/csv")
        }

        return ResponseEntity(
            FileSystemResource(tempFile),
            headers,
            HttpStatus.OK
        )
    }

    fun exportTasksStatisticsToCSV(): ResponseEntity<FileSystemResource> {
        val tempFile = File.createTempFile("tasks_statistics", ".csv")
        val writer = CSVWriter(FileWriter(tempFile))
        val users = userService.findAll()
        writer.writeNext(
            arrayOf(
                "Task ID",
                "Title",
                "Description",
                "Due Date",
                "Priority",
                "Status",
                "User ID",
                "Username"
            )
        )

        val tasks = taskRepository.findAll()
        for (task in tasks) {
            val user = users.find { it.id == task.user.id } ?: continue
            val line = arrayOf(
                task.id.toString(),
                task.title,
                task.description,
                task.dueDate.toString(),
                task.priority.toString(),
                task.status.name,
                task.user.id.toString(),
                user.username
            )
            writer.writeNext(line)
        }

        writer.close()

        val headers = org.springframework.http.HttpHeaders().apply {
            add(
                org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=tasks_and_statistics.csv"
            )
            add(org.springframework.http.HttpHeaders.CONTENT_TYPE, "text/csv")
        }

        return ResponseEntity(
            FileSystemResource(tempFile),
            headers,
            HttpStatus.OK
        )
    }
}
