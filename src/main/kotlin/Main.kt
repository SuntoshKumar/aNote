import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PeopleAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import data.Note
import navigation.CustomNavigationHost
import navigation.rememberNavController
import screens.*
import utils.*
import java.io.File
import kotlin.system.exitProcess


var cNote by mutableStateOf(Note())


fun main() = application {

    val isLoginEnable by remember { mutableStateOf(pref.getBoolean(SWITCH_LOCK, false)) }


    if (isLoginEnable) {
        LoginWindow(this)
    } else {
        MainWindow(this)
    }

    println(RealmDatabase.realm.configuration.path)


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainWindow(application: ApplicationScope) {
    Window(
        onCloseRequest = { application.exitApplication() },
        title = "aNote",
        icon = painterResource("linux_icon.png")
    ) {

        val screens = Screen.values().toList()
        val navController by rememberNavController(Screen.HomeScreen.name)
        val currentScreen by remember {
            navController.currentScreen
        }


        //deleteAllNote()
        MaterialTheme {

            Row(modifier = Modifier.fillMaxSize()) {
                NavigationRail(
                    modifier = Modifier.fillMaxHeight(),
                    elevation = 22.dp
                ) {
                    screens.forEach {
                        NavigationRailItem(
                            selected = currentScreen == it.name,
                            selectedContentColor = Color(0xff00e676),
                            icon = {
                                Icon(
                                    imageVector = it.icon,
                                    contentDescription = it.label
                                )
                            },
                            label = {
                                Text(it.label)
                            },
                            alwaysShowLabel = false,
                            onClick = {
                                navController.navigate(it.name)
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    CustomNavigationHost(navController = navController)
                }
            }


        }


        val clipboardManager = LocalClipboardManager.current

        MenuBar {
            Menu("File", mnemonic = 'F') {
                Item(text = "New note", shortcut = KeyShortcut(key = Key.N, shift = true, alt = true)) {
                    currentNote = Note()
                    navController.navigate("EditNoteView")
                }

                Item(text = "Export as zip", shortcut = KeyShortcut(key = Key.Z, shift = true, alt = true)) {

                    exoportNoteAsZipFile()
                }

                Item(text = "Import from text file", shortcut = KeyShortcut(key = Key.Z, shift = true, alt = true)) {

                    importNoteFromTextFile()
                }

                Item(text = "Backup", shortcut = KeyShortcut(key = Key.B, shift = true, alt = true, ctrl = true)) {

                    backup()
                }

                Item(text = "Restore", shortcut = KeyShortcut(key = Key.R, shift = true, alt = true, ctrl = true)) {
                    // unzip(File("/Users/samkrish/Desktop/Books/aNote_05_09_2023_580.zip"))
                    restore()
                }


                Item(
                    text = "Copy all",
                    enabled = cNote.noteText.isNotEmpty(),
                    shortcut = KeyShortcut(key = Key.C, shift = true, alt = true)
                ) {

                    clipboardManager.setText(AnnotatedString(cNote.noteText))
                }

                Item(
                    text = "Save",
                    enabled = cNote.noteText.isNotEmpty(),
                    shortcut = KeyShortcut(key = Key.S, shift = true, alt = true)
                ) {
                    saveAsText(cNote)
                }

                Menu(text = "Save as", mnemonic = null) {
                    Item(
                        text = "Save as text file",
                        enabled = cNote.noteText.isNotEmpty(),
                        shortcut = KeyShortcut(key = Key.T, shift = true, alt = true)
                    ) {
                        saveAsText(cNote)
                    }

                    Item(
                        text = "Save as pdf",
                        enabled = cNote.noteText.isNotEmpty(),
                        shortcut = KeyShortcut(key = Key.P, shift = true, alt = true)
                    ) {
                        saveSingleNoteAsPdf(cNote)
                    }

                    Item(
                        text = "Save as pdf",
                        enabled = cNote.noteText.isNotEmpty(),
                        shortcut = KeyShortcut(key = Key.P, shift = true, alt = true)
                    ) {
                        saveSingleNoteAsPdf(cNote)
                    }
                }


            }
        }
    }
}

@Composable
@Preview
fun LoginWindow(application: ApplicationScope) {

    var loginSuccess by remember { mutableStateOf(false) }

    val focusRequester by remember { mutableStateOf(FocusRequester()) }

    //var showProgress by remember { mutableStateOf(false) }

    var passwordState by remember { mutableStateOf("") }

    var errorText by remember { mutableStateOf(false) }


    MaterialTheme {
        Window(
            onCloseRequest = { application.exitApplication() },
            visible = !loginSuccess,
            title = "Login",
            state = WindowState(
                position = WindowPosition(Alignment.Center),
                width = 500.dp,
                height = 400.dp
            )
        ) {


            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Surface(modifier = Modifier.size(96.dp), shape = CircleShape) {
                    Image(
                        imageVector = Icons.Rounded.PeopleAlt, contentDescription = null,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Login", fontSize = 24.sp, fontFamily = CustomFonts.ubuntu_5)

                Spacer(modifier = Modifier.height(24.dp))

                Surface(shape = RoundedCornerShape(12.dp), modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
                    TextField(
                        value = passwordState, onValueChange = { passwordState = it },
                        placeholder = { Text("Enter your password...", textAlign = TextAlign.Center) },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.focusRequester(focusRequester)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

//                if (showProgress){
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(24.dp),
//                        color = Color(0xff00c853)
//                    )
//                }

                Text(if (errorText) "Wrong password!" else "", color = MaterialTheme.colors.error)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = {
                        if (pref.get(CURRENT_PWD, "") == passwordState) {
                            //showProgress = true
                            loginSuccess = true

                        } else {
                            loginSuccess = false
                            errorText = true
                            //showProgress = false

                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    //colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff00c853))
                ) {
                    Text("Login", modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 12.dp))
                }
            }

        }
    }

    if (loginSuccess) {
        MainWindow(application)
    }


    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
