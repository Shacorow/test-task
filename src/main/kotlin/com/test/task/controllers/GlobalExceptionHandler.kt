package com.test.task.controllers

import com.test.task.exceptions.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(TaskAlreadyHasStatusDoneException::class)
    fun handleTaskAlreadyHasStatusDone(e: TaskAlreadyHasStatusDoneException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(TaskNotFoundByTitleException::class)
    fun handleTaskNotFoundByTitleException(e: TaskNotFoundByTitleException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(TaskNotFoundException::class)
    fun handleTaskNotFoundException(e: TaskNotFoundException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UserAlreadyExistException::class)
    fun handleUserAlreadyExistException(e: UserAlreadyExistException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(e: UserNotFoundException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(WrongPasswordException::class)
    fun handleWrongPasswordException(e: WrongPasswordException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)
    }
    @ExceptionHandler(ExpiredTokenException::class)
    fun handleExpiredTokenException(e: ExpiredTokenException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(AccessForbiddenException::class)
    fun handleExpiredTokenException(e: AccessForbiddenException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): ResponseEntity<String> {
        return ResponseEntity("An unexpected error occurred: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
