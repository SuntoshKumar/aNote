package navigation

import Screen
import androidx.compose.runtime.Composable
import screens.*

@Composable
fun CustomNavigationHost(
    navController: NavController
) {

    NavigationHost(navController) {
        composable(Screen.HomeScreen.name) {
            HomeScreen(navController)
        }

        composable("EditNoteView") {


//            AnimatedVisibility(
//                visible = true,
//                enter = fadeIn() + slideInHorizontally(
//                    initialOffsetX = { 1000 },
//                    animationSpec = tween(durationMillis = 300)
//                ),
//                exit = fadeOut() + slideOutHorizontally(
//                    targetOffsetX = { -1000 },
//                    animationSpec = tween(durationMillis = 300)
//                )
//            ) {
//                EditNoteView(navController)
//            }

            EditNoteView(navController)


        }

        composable(Screen.ToDoList.name){
            ToDoList(navController)
        }

        composable(Screen.WatchScreen.name) {
            Watch(navController)
        }

        composable(Screen.User.name){
            User(navController)
        }


//        composable(Screen.NotificationsScreen.name) {
//            NotificationScreen(navController)
//        }
//
//        composable(Screen.SettingsScreen.name) {
//            SettingScreen(navController)
//        }
//
//        composable(Screen.ProfileScreens.name) {
//            ProfileScreen(navController)
//        }

    }.build()
}