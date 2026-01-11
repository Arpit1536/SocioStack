package com.arpit.sociostack.views.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arpit.sociostack.views.components.AdminHomeContent
import com.arpit.sociostack.vm.AnnouncementViewModel
import com.arpit.sociostack.vm.MemberViewModel

@Composable
fun AdminHomeScreen(
    onMembersClick: () -> Unit,
    onAnnouncementsClick: () -> Unit,
    memberViewModel: MemberViewModel,
    announcementViewModel: AnnouncementViewModel = viewModel(),
    onAddMemberClick: () -> Unit,
    onPostAnnouncementClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit,
) {
    val members by memberViewModel.members.collectAsState()
    val announcements by announcementViewModel.announcements.collectAsState()

    AdminHomeContent(
        members = members,
        announcements = announcements,
        onMembersClick = onMembersClick,
        onAnnouncementsClick = onAnnouncementsClick,
        onAddMemberClick = onAddMemberClick,
        onPostAnnouncementClick = onPostAnnouncementClick,
          onSettingsClick = onSettingsClick,
        onAboutClick = onAboutClick   ,

    )
}