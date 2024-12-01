package com.test.task.dto

data class UserDTO(
    var id: Long,
    var username: String,
    var password: String,
    var isAdministrator: Boolean,
    val tasks: MutableList<TaskDTO> =  mutableListOf()
)
