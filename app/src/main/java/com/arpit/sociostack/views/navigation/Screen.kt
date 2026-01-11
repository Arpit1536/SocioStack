package com.arpit.sociostack.views.navigation

sealed class Screen(val route: String) {

    object RoleSelection : Screen("role_selection")

    object AdminHome : Screen("admin_home")

    object MemberHome : Screen("member_home")

    object MemberManagement : Screen("member_management")

    object AnnouncementManagement : Screen("announcement_management")

    object AddMember : Screen("add_member?memberId={memberId}") {
        fun createRoute(memberId: String): String {
            return "add_member?memberId=$memberId"
        }
    }

    object PostAnnouncement : Screen("post_announcement")

    object MemberDetail : Screen("member_detail/{memberId}/{isAdmin}") {
        fun createRoute(memberId: String, isAdmin: Boolean) =
            "member_detail/$memberId/$isAdmin"
    }

    object AnnouncementDetail : Screen("announcement_detail/{announcementId}") {
        fun createRoute(announcementId: String) = "announcement_detail/$announcementId"
    }

    object Settings : Screen("settings")

    object About : Screen("about")
}