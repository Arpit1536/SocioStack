package com.arpit.sociostack.views.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arpit.sociostack.views.screens.*
import com.arpit.sociostack.vm.MemberViewModel
import com.arpit.sociostack.vm.AnnouncementViewModel
import com.arpit.sociostack.vm.RoleViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    val memberViewModel: MemberViewModel = viewModel()
    val announcementViewModel: AnnouncementViewModel = viewModel()
    val roleViewModel: RoleViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Screen.RoleSelection.route) {
            RoleSelectionScreen()
        }

        composable(Screen.AdminHome.route) {
            AdminHomeScreen(
                onMembersClick = {
                    navController.navigate(Screen.MemberManagement.route)
                },
                onAnnouncementsClick = {
                    navController.navigate(Screen.AnnouncementManagement.route)
                },
                memberViewModel = memberViewModel,
                onAddMemberClick = {
                    navController.navigate(Screen.AddMember.createRoute(""))
                },
                onPostAnnouncementClick = {
                    Log.d("NAV_TEST", "NavGraph navigating to PostAnnouncement")
                    navController.navigate(Screen.PostAnnouncement.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onAboutClick = {
                    navController.navigate(Screen.About.route)
                },
                onAnnouncementClick = { announcement ->
                    navController.navigate(
                        Screen.AnnouncementDetail.createRoute(announcement.id)
                    )
                }
            )
        }

        composable(Screen.PostAnnouncement.route) {
            PostAnnouncementScreen(
                onBack = { navController.popBackStack() },
                viewModel = announcementViewModel
            )
        }

        composable(Screen.MemberHome.route) {
            MemberHomeScreen(
                onMembersClick = {
                    navController.navigate(Screen.MemberManagement.route)
                },
                onAnnouncementsClick = {
                    navController.navigate(Screen.AnnouncementManagement.route)
                },
                onAnnouncementClick = { announcement ->
                    navController.navigate(
                        Screen.AnnouncementDetail.createRoute(announcement.id)
                    )
                },
                onMemberClick = { memberId ->
                    navController.navigate(
                        Screen.MemberDetail.createRoute(memberId, isAdmin = false)
                    )
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onAboutClick = {
                    navController.navigate(Screen.About.route)
                },
                memberViewModel = memberViewModel,
                announcementViewModel = announcementViewModel
            )
        }

        composable(Screen.MemberManagement.route) {
            MemberManagementScreen(
                onBack = { navController.popBackStack() },
                onMemberClick = { memberId ->
                    navController.navigate(
                        Screen.MemberDetail.createRoute(memberId, isAdmin = true)
                    )
                },
                memberViewModel = memberViewModel
            )
        }

        composable(Screen.AnnouncementManagement.route) {
            AnnouncementManagementScreen(
                isAdmin = true,
                viewModel = announcementViewModel,
                onBack = { navController.popBackStack() },
                onAnnouncementClick = { announcement ->
                    navController.navigate(
                        Screen.AnnouncementDetail.createRoute(announcement.id)
                    )
                }
            )
        }

        composable(
            route = Screen.AnnouncementDetail.route,
            arguments = listOf(
                navArgument("announcementId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val announcementId = backStackEntry.arguments!!.getString("announcementId")!!
            val announcements = announcementViewModel.announcements.collectAsState().value
            val announcement = announcements.find { it.id == announcementId }

            announcement?.let {
                AnnouncementDetailScreen(
                    announcement = it,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onAboutClick = {
                    navController.navigate(Screen.About.route)
                },
                roleViewModel = roleViewModel
            )
        }

        composable(Screen.About.route) {
            AboutScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "add_member?memberId={memberId}",
            arguments = listOf(
                navArgument("memberId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getString("memberId")

            AddEditMemberScreen(
                viewModel = memberViewModel,
                memberId = memberId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "member_detail/{memberId}/{isAdmin}",
            arguments = listOf(
                navArgument("memberId") {
                    type = NavType.StringType
                },
                navArgument("isAdmin") {
                    type = NavType.BoolType
                }
            )
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments!!.getString("memberId")!!
            val isAdmin = backStackEntry.arguments!!.getBoolean("isAdmin")

            MemberDetailScreen(
                memberId = memberId,
                isAdmin = isAdmin,
                memberViewModel = memberViewModel,
                onEditClick = {
                    navController.navigate(
                        Screen.AddMember.createRoute(it)
                    )
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}