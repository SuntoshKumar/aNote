package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PeopleAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import utils.CustomFonts
import utils.loadImageBitmaps
import java.io.File

@Composable
@Preview
fun AboutDeveloper(visibleDevState:MutableState<Boolean>) {

    val clipboardManager = LocalClipboardManager.current
    val uriHost = LocalUriHandler.current
    var facebookLinkClick by remember { mutableStateOf(false) }
    var githubLinkClick by remember { mutableStateOf(false) }

    //val resourcesDir = File(System.getProperty("compose.application.resources.dir"))


    Dialog(
        visible = visibleDevState.value,
        onCloseRequest = { visibleDevState.value = false},
        title = "About developer"
    ) {


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Surface(modifier = Modifier.size(100.dp), shape = CircleShape, elevation = 8.dp) {
//                Image(
//                    bitmap = loadImageBitmaps(resourcesDir.resolve("dev_duo.png")),
//                    contentScale = ContentScale.Crop,
//                    contentDescription = null
//                )

                Image(imageVector = Icons.Rounded.PeopleAlt, contentDescription = null,
                    modifier = Modifier.padding(all = 16.dp), alpha = 0.7f)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Developer Duo", fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = CustomFonts.sofia_11)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "suntosemyanmar@gmail.com",
                fontSize = 12.sp,
                modifier = Modifier.selectable(selected = true, enabled = true, role = Role.Button,
                    onClick = {
                        clipboardManager.setText(AnnotatedString("suntosemyanmar@gmail.com"))
                    })
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Facebook",
                fontSize = 12.sp,
                modifier = Modifier.selectable(selected = true, enabled = true, role = Role.Button,
                    onClick = {
                        facebookLinkClick = true
                    })
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Github",
                fontSize = 12.sp,
                modifier = Modifier.selectable(selected = true, enabled = true, role = Role.Button,
                    onClick = {
                        githubLinkClick = true
                    })
            )


        }

        if (facebookLinkClick){
            uriHost.openUri("https://www.facebook.com/SuntoshKumar.SamKrish")
        }

        if (githubLinkClick){
            uriHost.openUri("https://github.com/SuntoshKumar")
        }

    }
}
