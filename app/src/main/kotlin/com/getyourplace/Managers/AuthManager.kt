package com.getyourplace.Managers

import android.content.Context
import com.getyourplace.Models.UserProfile
import com.getyourplace.Models.UserRole // Ensure this import exists
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
        // Fix: Added .shared and ensured the generic type is passed correctly
        val savedProfile = CacheManager.shared.load<UserProfile>(context, profileKey)
        if (savedProfile != null) {
            _currentUser.value = savedProfile
            _isAuthenticated.value = true
        }
    }

    fun login(userProfile: UserProfile) {
        _currentUser.value = userProfile
        _isAuthenticated.value = true
        // Fix: Added .shared
        CacheManager.shared.save(context, userProfile, profileKey)
    }

    fun logout() {
        _isAuthenticated.value = false
        _currentUser.value = null
        // Fix: Added .shared
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