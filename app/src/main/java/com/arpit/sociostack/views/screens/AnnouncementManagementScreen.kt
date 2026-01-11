package com.arpit.sociostack.views.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arpit.sociostack.data.model.Announcement
import com.arpit.sociostack.views.components.AddAnnouncementDialog
import com.arpit.sociostack.views.components.AnnouncementCardGlassClickable
import com.arpit.sociostack.views.components.EmptyStateCard
import com.arpit.sociostack.views.components.GlassIconButton
import com.arpit.sociostack.vm.AnnouncementViewModel

@Composable
fun AnnouncementManagementScreen(
    isAdmin: Boolean = true,
    viewModel: AnnouncementViewModel = viewModel(),
    onBack: () -> Unit,
    onAnnouncementClick: (Announcement) -> Unit
) {
    val announcements by viewModel.announcements.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val gradientColors = listOf(
        Color(0xFF1A0B2E),
        Color(0xFF2D1B69),
        Color(0xFF4A148C)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(gradientColors))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlassIconButton(
                        icon = Icons.Default.ArrowBack,
                        onClick = onBack
                    )

                    Column {
                        Text(
                            text = "Announcements",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${announcements.size} active",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (announcements.isEmpty()) {
                EmptyStateCard(
                    icon = Icons.Default.Add,
                    message = if (isAdmin) "No announcements yet. Tap + to create one" else "No announcements yet"
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = if (isAdmin) 88.dp else 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(announcements) { announcement ->
                        AnnouncementCardGlassClickable(
                            title = announcement.title,
                            description = announcement.message,
                            time = announcement.timestamp?.toDate()?.toString() ?: "",
                            priority = announcement.priority,
                            onClick = { onAnnouncementClick(announcement) }
                        )
                    }
                }
            }
        }


        if (isAdmin) {
            FloatingActionButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                containerColor = Color(0xFF6366F1).copy(alpha = 0.3f),
                contentColor = Color.White,
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Announcement",
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        if (showDialog) {
            AddAnnouncementDialog(
                onDismiss = { showDialog = false },
                onAdd = { title, desc ->
                    viewModel.postAnnouncement(title, desc, priority = "Normal")
                    showDialog = false
                }
            )
        }
    }
}