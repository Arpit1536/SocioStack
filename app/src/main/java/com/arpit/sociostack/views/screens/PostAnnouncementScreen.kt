package com.arpit.sociostack.views.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arpit.sociostack.state.UiState
import com.arpit.sociostack.vm.AnnouncementViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import com.arpit.sociostack.views.components.GlassIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostAnnouncementScreen(
    onBack: () -> Unit,
    viewModel: AnnouncementViewModel = viewModel()
) {
    val context = LocalContext.current
    val postState by viewModel.postState.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Normal") }
    var priorityMenuExpanded by remember { mutableStateOf(false) }

    val gradientColors = listOf(
        Color(0xFF1A0B2E),
        Color(0xFF2D1B69),
        Color(0xFF4A148C)
    )

    LaunchedEffect(postState) {
        when (postState) {
            is UiState.Success -> {
                Toast.makeText(context, "Announcement posted", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
                onBack()
            }

            is UiState.Error -> {
                Toast.makeText(
                    context,
                    (postState as UiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> Unit
        }
    }

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
                            text = "Post Announcement",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Share important updates",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = {
                            Text(
                                "Title",
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White.copy(alpha = 0.9f),
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White.copy(alpha = 0.8f),
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = {
                            Text(
                                "Description",
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White.copy(alpha = 0.9f),
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White.copy(alpha = 0.8f),
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        minLines = 4
                    )
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    ExposedDropdownMenuBox(
                        expanded = priorityMenuExpanded,
                        onExpandedChange = {
                            priorityMenuExpanded = !priorityMenuExpanded
                        }
                    ) {
                        OutlinedTextField(
                            value = priority,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text(
                                    "Priority",
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Dropdown",
                                    tint = Color.White.copy(alpha = 0.7f)
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White.copy(alpha = 0.9f),
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                cursorColor = Color.White,
                                focusedLabelColor = Color.White.copy(alpha = 0.8f),
                                unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = priorityMenuExpanded,
                            onDismissRequest = {
                                priorityMenuExpanded = false
                            },
                            modifier = Modifier.background(Color(0xFF2D1B69))
                        ) {
                            listOf("Low", "Normal", "High").forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            item,
                                            color = Color.White
                                        )
                                    },
                                    onClick = {
                                        priority = item
                                        priorityMenuExpanded = false
                                    },
                                    modifier = Modifier.background(
                                        if (priority == item)
                                            Color.White.copy(alpha = 0.1f)
                                        else
                                            Color.Transparent
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    onClick = {
                        viewModel.postAnnouncement(
                            title = title,
                            message = description,
                            priority = priority
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6366F1).copy(alpha = 0.4f),
                        contentColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color.White.copy(alpha = 0.3f)
                    ),
                    enabled = postState !is UiState.Loading
                ) {
                    if (postState is UiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Post Announcement",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}