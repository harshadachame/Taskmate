package com.example.taskmate

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {
    val allTasks: Flow<List<Task>> = dao.getAllTasks()

    suspend fun insertTask(task: Task) = dao.insertTask(task)
    suspend fun updateTask(task: Task) = dao.updateTask(task)
    suspend fun deleteTask(task: Task) = dao.deleteTask(task)
    fun getTaskById(taskId: Int): Flow<Task?> {
        return dao.getTaskById(taskId)
    }
}