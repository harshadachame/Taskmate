package com.example.taskmate

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String = "",  // ✅ Fix: Default empty description
    val priority: String,  // "Low", "Medium", "High"
    val category: String,  // "Work", "Personal", etc.
    val isCompleted: Boolean = false,
    val dueDate: Long? = null
)
