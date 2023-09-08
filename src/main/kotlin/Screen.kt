import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Screens
 */
enum class Screen(
    val label: String,
    val icon: ImageVector
) {
    HomeScreen(
        label = "Home",
        icon = Icons.Rounded.StickyNote2
    ),

    ToDoList(
        label = "TODO",
        icon = Icons.Filled.Checklist
    ),

    WatchScreen(
        label = "StopWatch",
        icon = Icons.Filled.Timer
    ),

    User(
        label = "User",
        icon = Icons.Rounded.PeopleAlt
    )
}