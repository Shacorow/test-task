package com.test.task.exceptions

class TaskNotFoundByTitleException (id: Long) : ApplicationException("Task with title: $id not found")
