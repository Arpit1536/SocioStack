package com.arpit.sociostack.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arpit.sociostack.data.model.Member
import com.arpit.sociostack.data.repo.MemberRepository
import com.arpit.sociostack.state.UiState
import kotlinx.coroutines.flow.*

class MemberViewModel : ViewModel() {

    private val repository = MemberRepository()

    private val _members = MutableStateFlow<List<Member>>(emptyList())
    val members: StateFlow<List<Member>> = _members


    private val _deleteState = MutableStateFlow<UiState>(UiState.Idle)

    init {
        fetchMembers()
    }

    fun fetchMembers() {
        repository.fetchMembers { list ->
            _members.value = list
        }
    }

    private val _addMemberState = MutableStateFlow<UiState>(UiState.Idle)
    val addMemberState: StateFlow<UiState> = _addMemberState

    fun addMember(
        name: String,
        role: String,
        domain: String,
        contact: String?
    ) {
        if (name.isBlank() || role.isBlank() || domain.isBlank() || contact?.isBlank() == true) {
            _addMemberState.value = UiState.Error("All fields are required")
            return
        }

        _addMemberState.value = UiState.Loading

        val member = Member(
            name = name,
            role = role,
            domain = domain,
            contact = contact
        )

        repository.addMember(
            member = member,
            onSuccess = {
                fetchMembers()
                _addMemberState.value = UiState.Success
            },
            onError = {
                _addMemberState.value = UiState.Error(it)
            }
        )
    }

    fun resetAddMemberState() {
        _addMemberState.value = UiState.Idle
    }

    fun updateMember(memberId: String, member: Member) {
        repository.updateMember(
            memberId = memberId,
            updatedMember = member,
            onSuccess = {
                fetchMembers()
            },
            onError = {}
        )
    }

    fun getMemberById(id: String): StateFlow<Member?> {
        return members.map { list ->
            list.find { it.id == id }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
    }

    fun deleteMember(memberId: String) {
        _deleteState.value = UiState.Loading

        repository.deleteMember(
            memberId = memberId,
            onSuccess = {
                _deleteState.value = UiState.Success
                fetchMembers()
            },
            onError = {
                _deleteState.value = UiState.Error(it.message ?: "Failed to delete member")
            }
        )
    }


}