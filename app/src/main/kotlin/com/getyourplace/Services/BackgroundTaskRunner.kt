package com.getyourplace.Services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getyourplace.Models.BackgroundTaskStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BackgroundTaskRunner<T> : ViewModel() {

    // Equivalent to TaskState struct
    data class TaskState<T>(
        val status: BackgroundTaskStatus = BackgroundTaskStatus.NONE,
        val result: T? = null,
        val error: Throwable? = null
    )

    // Equivalent to @Published var state
    private val _state = MutableStateFlow(TaskState<T>())
    val state = _state.asStateFlow()

    // Equivalent to currentTask
    private var currentJob: Job? = null

    /**
     * Runs a task in the background.
     * 'operation' is the Kotlin equivalent of Swift's 'async throws -> T'
     */
    fun runInBackground(operation: suspend () -> T) {
        // Cancel existing job if running (Disposable logic)
        currentJob?.cancel()

        // Start a new Coroutine in the ViewModel scope
        currentJob = viewModelScope.launch {
            // Update status to Running on Main Thread
            updateState(BackgroundTaskStatus.RUNNING)

            try {
                // Execute the background work (Dispatchers.IO is best for network/disk)
                val result = withContext(Dispatchers.IO) {
                    operation()
                }

                // Success: Coroutines automatically handle cancellation checks
                // but we can check isActive if the operation is heavy
                updateState(BackgroundTaskStatus.COMPLETED, result = result)

            } catch (e: Exception) {
                // Failure
                updateState(BackgroundTaskStatus.FAILED, error = e)
            }
        }
    }

    /**
     * Ensures UI-bound property updates happen on the Main Thread.
     * In Kotlin, StateFlow updates are thread-safe, but we ensure
     * the logic feels familiar to the MainActor approach.
     */
    private suspend fun updateState(
        status: BackgroundTaskStatus,
        result: T? = null,
        error: Throwable? = null
    ) {
        withContext(Dispatchers.Main) {
            _state.value = TaskState(status = status, result = result, error = error)
        }
    }

    fun cancel() {
        currentJob?.cancel()
        currentJob = null
    }
}