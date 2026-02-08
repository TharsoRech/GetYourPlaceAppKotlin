package com.getyourplace.Helpers

import androidx.compose.runtime.MutableState

/**
 * Creates a helper for specific keys in a Map State
 * Equivalent to: binding.key("someKey")
 */
fun MutableState<Map<String, String>>.updateKey(key: String, newValue: String) {
    this.value = this.value.toMutableMap().apply {
        put(key, newValue)
    }
}

/**
 * Handles nullable String states
 * Equivalent to: .toUnwrapped
 */
fun MutableState<String?>.getUnwrapped(): String = this.value ?: ""