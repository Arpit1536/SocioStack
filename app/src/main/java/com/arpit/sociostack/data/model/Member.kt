package com.arpit.sociostack.data.model

data class Member(
    val id: String = "",
    val name: String = "",
    val role: String = "",
    val domain: String = "",
    val contact: String? = null,
    val profileImageUrl: String? = null
)