package screens

import StopWatch
import StopWatchDisplay
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import navigation.NavController

@Composable
fun Watch(navController: NavController){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val stopWatch = remember { StopWatch() }
        StopWatchDisplay(
            formattedTime = stopWatch.formattedTime,
            onStartClick = stopWatch::start,
            onPauseClick = stopWatch::pause,
            onResetClick = stopWatch::reset
        )
    }
}