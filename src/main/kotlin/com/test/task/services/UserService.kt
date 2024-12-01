package com.test.task.services

import com.test.task.bom.Role
import com.test.task.bom.User
import com.test.task.config.JwtTokenUtil
import com.test.task.exceptions.UserAlreadyExistException
import com.test.task.exceptions.UserNotFoundException
import com.test.task.exceptions.WrongPasswordException
import com.test.task.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired private val userRepository: UserRepository
) {

    fun register(username: String, password: String): User {
        val existingUser = userRepository.getUserByUsername(username)

        if (existingUser != null) {
            throw UserAlreadyExistException()
        }
        var user: User = User()
        user.username = username
        user.password = password
        user = userRepository.save(user)
        return user
    }

    fun login(username: String, password: String): String {
        val existingUser = userRepository.getUserByUsername(username)
            ?: throw UserNotFoundException()

        if (existingUser.password === password) {
            throw WrongPasswordException()
        }
        var role: String = Role.USER.name
        if (existingUser.isAdministrator) {
            role = Role.ADMIN.name
        }
        val token = JwtTokenUtil.generateToken(username, listOf(role))
        return token
    }

    fun getByUsername(username: String): User {
        return userRepository.getUserByUsername(username)
            ?: throw UserNotFoundException()

    }

    fun findAll(): MutableList<User> {
        return userRepository.findAll()
    }

    fun delete(id: Long) {
        userRepository.getReferenceById(id)
            ?: throw UserNotFoundException()
        userRepository.deleteById(id)
    }

    fun update(newPassword: String, username: String): User {
        var existingUser = userRepository.getUserByUsername(username)
            ?: throw UserNotFoundException()
        existingUser.password = newPassword
        existingUser = userRepository.save(existingUser)
        return existingUser
    }

    fun changeUserRole(id: Long): User {
        var user = userRepository.getReferenceById(id)
            ?: throw UserNotFoundException()
        user.isAdministrator = !user.isAdministrator
        user = userRepository.save(user)
        return user
    }
}
