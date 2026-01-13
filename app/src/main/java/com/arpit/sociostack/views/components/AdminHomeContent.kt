package com.arpit.sociostack.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arpit.sociostack.data.model.Announcement
import com.arpit.sociostack.data.model.Member

@Composable
fun AdminHomeContent(
    members: List<Member>,
    announcements: List<Announcement>,
    onMembersClick: () -> Unit,
    onAnnouncementsClick: () -> Unit,
    onAddMemberClick: () -> Unit,
    onPostAnnouncementClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit,
    onAnnouncementClick: (Announcement) -> Unit
) {
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
                Column {
                    Text(
                        text = "Admin Dashboard",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Manage your society",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GlassIconButton(
                        icon = Icons.Default.Info,
                        onClick = onAboutClick
                    )
                    GlassIconButton(
                        icon = Icons.Default.Settings,
                        onClick = onSettingsClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionButton(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.PersonAdd,
                    text = "Add Member",
                    onClick = onAddMemberClick
                )

                QuickActionButton(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Announcement,
                    text = "Post",
                    onClick = onPostAnnouncementClick
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            SectionHeader(
                title = "Members",
                subtitle = "${members.size} total",
                onViewAllClick = onMembersClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (members.isEmpty()) {
                EmptyStateCard(
                    icon = Icons.Default.People,
                    message = "No members yet"
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(members) { member ->
                        MemberCardGlass(
                            member = member,
                            onClick = onMembersClick
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            SectionHeader(
                title = "Announcements",
                subtitle = "${announcements.size} active",
                onViewAllClick = onAnnouncementsClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (announcements.isEmpty()) {
                EmptyStateCard(
                    icon = Icons.Default.Announcement,
                    message = "No announcements yet"
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(announcements) { announcement ->
                        AnnouncementCardGlass(
                            title = announcement.title,
                            description = announcement.message,
                            time = announcement.timestamp?.toDate().toString(),
                            priority = announcement.priority,
                            onClick = { onAnnouncementClick(announcement) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GlassIconButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun QuickActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6366F1).copy(alpha = 0.3f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Color.White.copy(alpha = 0.3f)
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    onViewAllClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f)
            )
        }

        TextButton(onClick = onViewAllClick) {
            Text(
                "View all",
                color = Color(0xFFBDBDFF),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "View all",
                tint = Color(0xFFBDBDFF),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun MemberCardGlass(
    member: Member,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Color.White.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.1f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF6366F1),
                                Color(0xFF8B5CF6)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.name.firstOrNull()?.uppercase() ?: "?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = member.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 1
            )

            Text(
                text = member.role,
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.6f),
                maxLines = 1
            )
        }
    }
}

@Composable
fun AnnouncementCardGlass(
    title: String,
    description: String,
    time: String,
    priority: String = "Medium",
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Color.White.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.1f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        when (priority.lowercase()) {
                            "high" -> Color(0xFFEF4444).copy(alpha = 0.3f)
                            "low" -> Color(0xFF10B981).copy(alpha = 0.3f)
                            else -> Color(0xFFF59E0B).copy(alpha = 0.3f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Announcement,
                    contentDescription = null,
                    tint = when (priority.lowercase()) {
                        "high" -> Color(0xFFFFB3B3)
                        "low" -> Color(0xFFB3FFD9)
                        else -> Color(0xFFFFE4B3)
                    },
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = when (priority.lowercase()) {
                            "high" -> Color(0xFFEF4444).copy(alpha = 0.3f)
                            "low" -> Color(0xFF10B981).copy(alpha = 0.3f)
                            else -> Color(0xFFF59E0B).copy(alpha = 0.3f)
                        }
                    ) {
                        Text(
                            text = priority,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = time,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun EmptyStateCard(
    icon: ImageVector,
    message: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.05f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.3f),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}