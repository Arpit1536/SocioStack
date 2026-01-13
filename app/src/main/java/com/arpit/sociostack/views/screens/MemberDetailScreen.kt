package com.arpit.sociostack.views.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.arpit.sociostack.state.UiState
import com.arpit.sociostack.vm.MemberViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDetailScreen(
    memberId: String,
    isAdmin: Boolean,
    onEditClick: (String) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onBack: () -> Unit,
    memberViewModel: MemberViewModel = viewModel()
) {
    val context = LocalContext.current
    val member by memberViewModel.getMemberById(memberId).collectAsState(initial = null)
    val deleteState by memberViewModel.deleteState.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(deleteState) {
        when (deleteState) {
            is UiState.Success -> {
                Toast.makeText(
                    context,
                    "Member deleted successfully",
                    Toast.LENGTH_SHORT
                ).show()
                memberViewModel.resetDeleteState()
                onDeleteConfirmed()
            }
            is UiState.Error -> {
                Toast.makeText(
                    context,
                    (deleteState as UiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                memberViewModel.resetDeleteState()
            }
            else -> Unit
        }
    }

    val gradientColors = listOf(
        Color(0xFF1A0B2E),
        Color(0xFF2D1B69),
        Color(0xFF4A148C)
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Member Details",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(gradientColors))
                .padding(padding)
        ) {
            member?.let { memberData ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    GlassCard {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
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
                                if (!memberData.profileImageUrl.isNullOrEmpty()) {
                                    AsyncImage(
                                        model = memberData.profileImageUrl,
                                        contentDescription = "Profile",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Text(
                                        text = memberData.name.firstOrNull()?.uppercase() ?: "?",
                                        fontSize = 48.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = memberData.name,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color(0xFF6366F1).copy(alpha = 0.3f),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    Color(0xFF6366F1).copy(alpha = 0.5f)
                                )
                            ) {
                                Text(
                                    text = memberData.role,
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 8.dp
                                    ),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFBDBDFF)
                                )
                            }
                        }
                    }

                    GlassCard {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Information",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )

                            InfoRowGlass(
                                icon = Icons.Default.Work,
                                label = "Domain",
                                value = memberData.domain
                            )

                            InfoRowGlass(
                                icon = Icons.Default.Person,
                                label = "Role",
                                value = memberData.role
                            )

                            if (!memberData.contact.isNullOrEmpty()) {
                                InfoRowGlass(
                                    icon = Icons.Default.Person,
                                    label = "Contact",
                                    value = memberData.contact!!
                                )
                            }

                            InfoRowGlass(
                                icon = Icons.Default.Person,
                                label = "Member ID",
                                value = memberData.id.take(8)
                            )
                        }
                    }

                    if (isAdmin) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            GlassButton(
                                onClick = { onEditClick(memberData.id) },
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.Edit,
                                text = "Edit",
                                containerColor = Color(0xFF6366F1).copy(alpha = 0.2f),
                                contentColor = Color(0xFFBDBDFF),
                                enabled = deleteState !is UiState.Loading
                            )

                            GlassButton(
                                onClick = { showDeleteDialog = true },
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.Delete,
                                text = "Delete",
                                containerColor = Color(0xFFEF4444).copy(alpha = 0.2f),
                                contentColor = Color(0xFFFFB3B3),
                                enabled = deleteState !is UiState.Loading
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            } ?: run {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF6366F1)
                    )
                }
            }

            if (deleteState is UiState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    "Delete Member",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete ${member?.name}? This action cannot be undone.",
                    color = Color(0xFFD1D5DB)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        memberViewModel.deleteMember(memberId)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF4444)
                    )
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = Color(0xFFBDBDFF))
                }
            },
            containerColor = Color(0xFF1F2937),
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Color.White.copy(alpha = 0.2f)
        )
    ) {
        Box(
            modifier = Modifier.background(
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.1f),
                        Color.White.copy(alpha = 0.05f)
                    )
                )
            )
        ) {
            content()
        }
    }
}

@Composable
fun InfoRowGlass(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF6366F1),
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun GlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    containerColor: Color,
    contentColor: Color,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = containerColor.copy(alpha = 0.5f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            contentColor.copy(alpha = 0.3f)
        ),
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = contentColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = contentColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}