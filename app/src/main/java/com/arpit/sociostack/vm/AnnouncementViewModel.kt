package com.arpit.sociostack.vm

import androidx.lifecycle.ViewModel
import com.arpit.sociostack.data.model.Announcement
import com.arpit.sociostack.data.repo.AnnouncementRepository
import com.arpit.sociostack.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AnnouncementViewModel : ViewModel() {

    private val repository = AnnouncementRepository()


    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    private val _announcements =
        MutableStateFlow<List<Announcement>>(emptyList())
    val announcements: StateFlow<List<Announcement>> = _announcements

    init {

        fetchAnnouncements()
    }


    fun fetchAnnouncements() {
        _uiState.value = UiState.Loading

        repository.fetchAnnouncements(
            onResult = { list ->
                _announcements.value = list
                _uiState.value = UiState.Success
            },
            onError = { e ->
                _uiState.value =
                    UiState.Error(e.message ?: "Failed to load announcements")
            }
        )
    }




    private val _postState = MutableStateFlow<UiState>(UiState.Idle)
    val postState: StateFlow<UiState> = _postState

    fun postAnnouncement(title: String, message: String, priority: String) {
        if (title.isBlank() || message.isBlank()) {
            _postState.value = UiState.Error("Fields cannot be empty")
            return
        }

        _postState.value = UiState.Loading

        repository.addAnnouncement(
            title = title,
            message = message,
            priority = priority,
            onSuccess = {
                _postState.value = UiState.Success
            },
            onError = { e ->
                _postState.value = UiState.Error(
                    e.message ?: "Something went wrong"
                )
            }
        )
    }


    fun resetState() {
        _uiState.value = UiState.Idle
    }
}
