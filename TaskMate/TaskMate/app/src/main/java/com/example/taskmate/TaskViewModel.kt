package com.example.taskmate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    val allTasks: StateFlow<List<Task>> = repository.allTasks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    fun addTask(task: Task) = viewModelScope.launch { repository.insertTask(task) }
    fun updateTask(task: Task) = viewModelScope.launch { repository.updateTask(task) }
    fun deleteTask(task: Task) = viewModelScope.launch { repository.deleteTask(task) }
    fun getTaskById(taskId: Int): Flow<Task?> { return repository.getTaskById(taskId) }
}

/*class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // Use MutableLiveData to hold the list of tasks
    private val _allTasks = MutableLiveData<List<Task>>()
    val allTasks: LiveData<List<Task>> get() = _allTasks // Exposed as immutable LiveData

    init {
        fetchTasks() // Load tasks initially
    }

    private fun fetchTasks() {
        viewModelScope.launch {
            repository.allTasks.collect { tasks ->
                _allTasks.postValue(tasks) // Collects Flow and updates LiveData
            }
        }
    }

    fun addTask(task: Task) = viewModelScope.launch { repository.insertTask(task) }
    fun updateTask(task: Task) = viewModelScope.launch { repository.updateTask(task) }
    fun deleteTask(task: Task) = viewModelScope.launch { repository.deleteTask(task) }

    // Convert Flow to LiveData for getting a task by ID
    fun getTaskById(taskId: Int): LiveData<Task?> = repository.getTaskById(taskId).asLiveData()
}*/
