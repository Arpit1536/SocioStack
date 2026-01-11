package com.arpit.sociostack.data.model

import com.google.firebase.Timestamp


data class Announcement(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val priority: String = "Normal", // High / Normal
    val timestamp: Timestamp? = null)
