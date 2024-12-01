package com.test.task.exceptions

open class TaskAlreadyHasStatusDoneException(id: Long) : ApplicationException("Task with id: $id already has status done")
