package com.arpit.sociostack.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arpit.sociostack.data.local.RolePreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoleViewModel(application: Application) : AndroidViewModel(application) {

    private val rolePrefs = RolePreferences(application)


    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _role = MutableStateFlow<String?>(null)
    val role: StateFlow<String?> = _role.asStateFlow()

    init {

        viewModelScope.launch {
            rolePrefs.roleFlow.collect { savedRole ->
                _role.value = savedRole
                _isLoading.value = false
            }
        }
    }

    fun selectRole(role: String) {
        viewModelScope.launch {
            rolePrefs.saveRole(role)
        }
    }

    fun logout() {
        viewModelScope.launch {
            rolePrefs.clearRole()
        }
    }
}