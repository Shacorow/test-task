package com.test.task.controllers

import com.test.task.convertor.UserConvertor
import com.test.task.dto.AuthRequest
import com.test.task.dto.TaskDTO
import com.test.task.dto.UserDTO
import com.test.task.services.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Handles user registration, login, and logout operations")
class AuthController(private val userService: UserService, @Autowired private val userConvertor: UserConvertor) {

    @PostMapping("/registration")
    @Operation(
        summary = "User Registration",
        description = "Registers a new user in the system by providing a username and password."
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
                responseCode = "409",
                description = "User already exists",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    fun register(@RequestBody authRequest: AuthRequest): ResponseEntity<String> {
        return try {
            val user = userService.register(authRequest.username, authRequest.password)
            ResponseEntity.ok("User ${user.username} registered successfully")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body("Error: ${e.message}")
        }
    }

    @PostMapping("/login")
    @Operation(
        summary = "User Login",
        description = "Logs in a user by authenticating their username and password, and returns a JWT token for session management."
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
                responseCode = "409",
                description = "User already exists",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    fun login(@RequestBody authRequest: AuthRequest): ResponseEntity<Map<String, String>> {
        val token = userService.login(authRequest.username, authRequest.password)
        return ResponseEntity.ok(mapOf("token" to token))
    }

    @PostMapping("/logout")
    @Operation(
        summary = "User Logout",
        description = "Logs out the currently authenticated user by clearing the authentication context."
    )
    fun logout(): ResponseEntity<Any> {
        SecurityContextHolder.clearContext()
        return ResponseEntity.ok("Exit completed")
    }
}
