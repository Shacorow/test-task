package com.test.task.controllers

import com.test.task.convertor.UserConvertor
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
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/user")
@Tag(name = "User", description = "For management user data")
class UserController(private val userService: UserService, @Autowired private val userConvertor: UserConvertor) {

    @PatchMapping("/password")
    @Operation(
        summary = "User update",
        description = "User can update his password"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "User successfully updated",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input data provided",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    fun update(@RequestBody newPassword: String): UserDTO {
        val username = SecurityContextHolder.getContext().authentication.name
        val user = userService.update(newPassword, username)
        val userDTO: UserDTO = UserDTO(0, "", "", false)
        userConvertor.toDTO(user, userDTO)
        return userDTO
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
        summary = "Delete User",
        description = "Deletes a user from the system by their unique ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "User deleted successful",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = String::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized: User is not authenticated",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
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
    fun deleteUser(@PathVariable(value = "id") id: Long): String {
        userService.delete(id)
        return "User has been deleted"
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
        summary = "Change User Role",
        description = "Changes the role of a user identified by their unique ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "User role changed successfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = String::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized: User is not authenticated",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
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
    fun changeUserRole(@PathVariable(value = "id") id: Long): UserDTO {
        val user = userService.changeUserRole(id)
        val userDTO: UserDTO = UserDTO(0, "", "", false)
        userConvertor.toDTO(user, userDTO)
        return userDTO
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
        summary = "Find All Users",
        description = "Retrieves a list of all users in the system."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Find all users successful",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserDTO::class)
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
    fun findAllUsers(): List<UserDTO> {
        val users = userService.findAll()
        val userDTOs: MutableList<UserDTO> = mutableListOf()
        for (user in users) {
            val userDTO: UserDTO = UserDTO(0, "", "", false)
            userConvertor.toDTO(user, userDTO)
            userDTOs.add(userDTO)
        }
        return userDTOs
    }
}
