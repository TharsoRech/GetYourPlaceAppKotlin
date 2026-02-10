package com.getyourplace.Managers

import android.content.Context
import com.getyourplace.Models.UserProfile
import com.getyourplace.Models.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthManager(private val context: Context) {

    private val profileKey = "user_profile_cache"

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    init {
        // Wrap in try-catch to prevent crashes in Previews or corrupted cache scenarios
        try {
            val savedProfile = CacheManager.shared.load<UserProfile>(context, profileKey)
            if (savedProfile != null) {
                _currentUser.value = savedProfile
                _isAuthenticated.value = true
            }
        } catch (e: Exception) {
            // In a real app, log this to Firebase or Sentry
            println("AuthManager: Failed to load cached profile: ${e.message}")
        }
    }

    fun login(userProfile: UserProfile) {
        _currentUser.value = userProfile
        _isAuthenticated.value = true
        CacheManager.shared.save(context, userProfile, profileKey)
    }

    fun logout() {
        _isAuthenticated.value = false
        _currentUser.value = null
        CacheManager.shared.remove(context, profileKey)
    }

    companion object {
        fun mock(context: Context, role: UserRole): AuthManager {
            val auth = AuthManager(context)
            // Note: In a real app, you'd create a mock UserProfile model
            auth._currentUser.value = UserProfile(role = role)
            auth._isAuthenticated.value = true
            return auth
        }
    }
}