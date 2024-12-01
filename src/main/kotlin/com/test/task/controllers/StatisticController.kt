package com.test.task.controllers

import com.test.task.convertor.TaskConvertor
import com.test.task.convertor.UserConvertor
import com.test.task.dto.CreateTaskDTO
import com.test.task.dto.TaskDTO
import com.test.task.dto.UserDTO
import com.test.task.dto.UserStatistic
import com.test.task.services.StatisticService
import com.test.task.services.TaskService
import com.test.task.services.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.FileSystemResource
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/statistic")
@Tag(name = "Statistic", description = "Admin endpoints for system statistics.")
class StatisticController(
    private val statisticService: StatisticService,
    private val taskService: TaskService,
    @Autowired private val taskConvertor: TaskConvertor,
) {

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
        summary = "Get User Statistics",
        description = "Retrieves statistics about user activities."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Get user statistics successfully",
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
    fun getStatisticByUsers(): MutableList<UserStatistic> {
        return taskService.getStatisticByUsers()
    }

    @GetMapping("/export/tasks")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
        summary = "Export Task Statistics",
        description = "Exports task statistics in CSV format."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Export statistics successfully",
                content = [Content(mediaType = "application/csv")]
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
    fun exportStatisticTasks(): ResponseEntity<FileSystemResource> {
        return statisticService.exportTasksStatisticsToCSV()
    }

    @GetMapping("/export/users")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
        summary = "Export Users Statistics",
        description = "Exports users statistics in CSV format."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Export statistics successfully",
                content = [Content(mediaType = "application/csv")]
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
    fun exportStatisticUsers(): ResponseEntity<FileSystemResource> {
        return statisticService.exportUsersStatisticsToCSV()
    }

    @GetMapping("/tasks/{status}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
        summary = "Get Task Statistics by Status",
        description = "Retrieves task statistics filtered by the specified status."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Get task statistics successfully",
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
    fun getStatisticByStatus(@PathVariable(value = "status") status: String): List<TaskDTO> {
        val tasks = taskService.getStatisticByStatus(status)
        val taskDTOs: MutableList<TaskDTO> = mutableListOf()
        if (tasks != null) {
            for (task in tasks) {
                val taskDTO = TaskDTO()
                taskConvertor.toDTO(task, taskDTO)
                taskDTOs.add(taskDTO)
            }
        }
        return taskDTOs
    }
}
