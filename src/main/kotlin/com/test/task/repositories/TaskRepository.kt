package com.test.task.repositories

import com.test.task.bom.Status
import com.test.task.bom.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    fun getTaskById(id: Long): Task?

    fun findAllByStatus(status: Status): List<Task>?
    fun getTaskByIdAndUserId(id: Long, userId: Long): Task?
    fun findTaskByStatusAndUserId(status: Status, userId: Long): List<Task>?

    fun findAllByUserId(id: Long): List<Task>?

}
