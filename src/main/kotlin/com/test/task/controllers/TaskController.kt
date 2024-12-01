package com.test.task.controllers

import com.test.task.convertor.TaskConvertor
import com.test.task.dto.CreateTaskDTO
import com.test.task.dto.TaskDTO
import com.test.task.services.TaskService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/task")
@Tag(name = "Task Management", description = "API for task management")
class TaskController(private val taskService: TaskService, @Autowired private val taskConvertor: TaskConvertor) {

    @PostMapping("/")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
        summary = "Create a new task",
        description = "Allows the user to create a new task"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Task successfully created",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = CreateTaskDTO::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input data provided",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized: User is not authenticated",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @SecurityRequirement(name = "Bearer Authentication")
    fun create(@RequestBody createTaskDTO: CreateTaskDTO): TaskDTO {
        val username = SecurityContextHolder.getContext().authentication.name
        val task = taskService.create(createTaskDTO, username)
        val taskDTO = TaskDTO()
        taskConvertor.toDTO(task, taskDTO)
        return taskDTO
    }

    @PatchMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Changing task status to done", description = "Changes the status of a task to done")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Task successfully created",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = TaskDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input data provided",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Task not found",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized: User is not authenticated",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @SecurityRequirement(name = "Bearer Authentication")
    fun changeStatus(@PathVariable(value = "id") id: Long): TaskDTO {
        val username = SecurityContextHolder.getContext().authentication.name
        val task = taskService.changeStatus(id, username)
        val taskDTO: TaskDTO = TaskDTO()
        if (task != null) {
            taskConvertor.toDTO(task, taskDTO)
        }
        return taskDTO
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Update task", description = "Updates task data")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Task successfully created",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = TaskDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input data provided",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Task not found",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized: User is not authenticated",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @SecurityRequirement(name = "Bearer Authentication")
    fun update(@PathVariable(value = "id") id: Long, @RequestBody createTaskDTO: CreateTaskDTO): TaskDTO {
        val username = SecurityContextHolder.getContext().authentication.name
        val task = taskService.update(id, createTaskDTO, username)
        val result: TaskDTO = TaskDTO()
        if (task != null) {
            taskConvertor.toDTO(task, result)
        }
        return result
    }

    @GetMapping("/status/")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
        summary = "Getting a list of tasks",
        description = "Returns a list of tasks by status or all tasks without status"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Task successfully created",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = TaskDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input data provided",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized: User is not authenticated",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @SecurityRequirement(name = "Bearer Authentication")
    fun findALl(
        @RequestParam(value = "status", required = false) status: String?
    ): MutableList<TaskDTO> {
        val finalStatus = status ?: ""
        val taskDTOs: MutableList<TaskDTO> = mutableListOf()
        val username = SecurityContextHolder.getContext().authentication.name
        val tasks = taskService.findAllByStatus(finalStatus, username)
        for (task in tasks) {
            val taskDTO = TaskDTO()
            taskConvertor.toDTO(task, taskDTO)
            taskDTOs.add(taskDTO)
        }
        return taskDTOs
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Delete task", description = "Deletes a task by its title")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Task successfully created",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = TaskDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input data provided",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Task not found",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized: User is not authenticated",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @SecurityRequirement(name = "Bearer Authentication")
    fun delete(@PathVariable(value = "id") id: Long): String {
        val username = SecurityContextHolder.getContext().authentication.name
        taskService.delete(id, username)
        return "Task deleted"
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get task by id", description = "Get a task by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Task successfully found",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = TaskDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Task not found",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized: User is not authenticated",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @SecurityRequirement(name = "Bearer Authentication")
    fun getById(@PathVariable(value = "id") id: Long): TaskDTO {
        val taskDTO = TaskDTO()
        val task = taskService.getById(id)
        taskConvertor.toDTO(task, taskDTO)
        return taskDTO
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
        summary = "Find All Tasks",
        description = "Retrieves a list of all tasks in the system."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Find all tasks successful",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = TaskDTO::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized: User is not authenticated",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden: User does not have sufficient permissions",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @SecurityRequirement(name = "Bearer Authentication")
    fun findAllTasks(): List<TaskDTO> {
        val tasks = taskService.findAll()
        val taskDTOs: MutableList<TaskDTO> = mutableListOf()
        for (task in tasks) {
            val taskDTO = TaskDTO()
            taskConvertor.toDTO(task, taskDTO)
            taskDTOs.add(taskDTO)
        }
        return taskDTOs
    }
}
