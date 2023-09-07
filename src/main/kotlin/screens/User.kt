package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import navigation.NavController
import ui.AboutDeveloper
import utils.CustomFonts
import utils.backup
import utils.restore
import java.io.File
import java.util.prefs.Preferences

const val SWITCH_LOCK = "SWITCH_LOCK"
const val CURRENT_PWD = "CURRENT_PWD"
const val PREF_NODE = "SETTING_NODE"
const val SAVE_WATCH = "AUTO_SAVE_WATCH"
const val USE_LINEAR_LAYOUT = "USE_LINEAR_LAYOUT"
val pref = Preferences.userRoot().node(PREF_NODE)

@Composable
fun User(navController: NavController) {


    var isChecked by remember { mutableStateOf(pref.getBoolean(SWITCH_LOCK, false)) }
    val showConfirmPassword = remember { mutableStateOf(false) }
    val currentPwd by remember { mutableStateOf(pref.get(CURRENT_PWD, "")) }
    var autoSaveStopWatch by remember { mutableStateOf(pref.getBoolean(SAVE_WATCH, false)) }
    var useLinearLayout by remember { mutableStateOf(pref.getBoolean(USE_LINEAR_LAYOUT, false)) }
    var showDeveloperWindow = remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            shape = CircleShape,
            modifier = Modifier.size(100.dp)
        ) {
            Image(
                imageVector = Icons.Rounded.PeopleAlt, contentDescription = null,
                alpha = 0.7f,
                modifier = Modifier.padding(all = 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Welcome, Dear User",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = CustomFonts.ubuntu_5
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Version : 1.0 ", color = Color.Gray, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
                .weight(1f)
        ) {


            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(start = 36.dp, end = 36.dp)
                    //.height(100.dp)
                    .fillMaxWidth()
            ) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Image(
                            imageVector = Icons.Rounded.Lock, contentDescription = null,
                            modifier = Modifier.padding(start = 36.dp, end = 36.dp)
                        )

                        Column(modifier = Modifier.weight(1f)) {

                            Text("Lock")

                            Spacer(modifier = Modifier.height(4.dp))

                            Text("Secure your note with lock screen", fontSize = 12.sp, color = Color.Gray)
                        }


                        Switch(
                            checked = isChecked, onCheckedChange = {
                                isChecked = it
                                pref.putBoolean(SWITCH_LOCK, it)
                            },
                            modifier = Modifier.padding(end = 28.dp)
                        )


                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isChecked) {
                        TextButton(onClick = {
                            showConfirmPassword.value = true
                        }, modifier = Modifier.padding(end = 16.dp)) {
                            Text("Set password")
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                }

            }

            SettingViews(
                Icons.Rounded.GridView,
                "Note layout style",
                "Enable linear layout for note",
                useLinearLayout
            ) {
                useLinearLayout = it
                pref.putBoolean(USE_LINEAR_LAYOUT, it)
            }

            SettingViews(
                Icons.Rounded.Timer,
                "Auto Save",
                "Enable to save stopwatch history automatically to note",
                autoSaveStopWatch
            ) {
                autoSaveStopWatch = it
                pref.putBoolean(SAVE_WATCH, it)
            }

            BackupView()

            RestoreView()

        }


        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            TextButton(onClick = {
                showDeveloperWindow.value = true
            }) {
                Text(
                    "About developer",
                    textDecoration = TextDecoration.Underline, color = Color.Blue, fontSize = 12.sp
                )
            }
        }

    }

    if (currentPwd.isNotEmpty()) {
        ConfirmPwd(currentPwd, showConfirmPassword)
    } else {
        NewPasswordWindow(showConfirmPassword)
    }

    AboutDeveloper(showDeveloperWindow)


}

@Composable
fun SettingViews(
    icons: ImageVector,
    settingName: String,
    secondText: String,
    isChecked: Boolean = false,
    onChange: (Boolean) -> Unit
) {

    Spacer(modifier = Modifier.height(8.dp))

    Surface(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(start = 36.dp, end = 36.dp)
            .height(64.dp)
            .fillMaxWidth()
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Image(
                imageVector = icons, contentDescription = null,
                modifier = Modifier.padding(start = 36.dp, end = 36.dp)
            )

            Column(modifier = Modifier.weight(1f)) {

                Text(settingName)

                Spacer(modifier = Modifier.height(4.dp))

                Text(secondText, fontSize = 12.sp, color = Color.Gray)
            }


            Switch(
                checked = isChecked, onCheckedChange = {
                    onChange(it)
                },
                modifier = Modifier.padding(end = 28.dp)
            )
        }
    }
}

@Composable
fun BackupView() {

    Spacer(modifier = Modifier.height(8.dp))

    Surface(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(start = 36.dp, end = 36.dp)
            .height(64.dp)
            .fillMaxWidth()
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Image(
                imageVector = Icons.Rounded.Backup, contentDescription = null,
                modifier = Modifier.padding(start = 36.dp, end = 36.dp)
            )

            Column(modifier = Modifier.weight(1f)) {

                Text("Backup")

                Spacer(modifier = Modifier.height(4.dp))

                Text("Backup all notes to disk", fontSize = 12.sp, color = Color.Gray)
            }

            TextButton(onClick = { backup() }, modifier = Modifier.padding(end = 28.dp)){
                Text("Backup")
            }
        }
    }
}

@Composable
fun RestoreView() {

    Spacer(modifier = Modifier.height(8.dp))

    Surface(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(start = 36.dp, end = 36.dp)
            .height(64.dp)
            .fillMaxWidth()
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Image(
                imageVector = Icons.Rounded.Restore, contentDescription = null,
                modifier = Modifier.padding(start = 36.dp, end = 36.dp)
            )

            Column(modifier = Modifier.weight(1f)) {

                Text("Restore")

                Spacer(modifier = Modifier.height(4.dp))

                Text("Restore note form disk", fontSize = 12.sp, color = Color.Gray)
            }

            TextButton(onClick = { restore() }, modifier = Modifier.padding(end = 28.dp)){
                Text("Restore")
            }
        }
    }
}

@Composable
fun ConfirmPwd(
    currentPwd: String,
    visibility: MutableState<Boolean> = mutableStateOf(false)
) {

    val showNewPwdWindow = remember { mutableStateOf(false) }

    val focusRequester by remember { mutableStateOf(FocusRequester()) }

    var passwordState by remember { mutableStateOf("") }

    var errorText by remember { mutableStateOf(false) }

    MaterialTheme {
        Window(
            onCloseRequest = { visibility.value = false },
            visible = visibility.value,
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

                Text(if (errorText) "Wrong password!" else "", color = MaterialTheme.colors.error)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = {
                        if (currentPwd == passwordState) {
                            showNewPwdWindow.value = true
                            visibility.value = false
                            errorText = false
                        } else {
                            showNewPwdWindow.value = false
                            errorText = true
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

    if (showNewPwdWindow.value) {
        NewPasswordWindow(showNewPwdWindow)
    }


    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun NewPasswordWindow(visibility: MutableState<Boolean> = mutableStateOf(false)) {

    var newPassState by remember { mutableStateOf("") }
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    var lengthError by remember { mutableStateOf(false) }

    Dialog(
        visible = visibility.value, onCloseRequest = {
            visibility.value = false
        },
        title = "Create New Password"
    ) {


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text("Create new password", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Surface(shape = CircleShape) {
                TextField(
                    value = newPassState, onValueChange = {
                        newPassState = it

                    },
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

            if (lengthError) {
                Text("Password length must be at least 6 character", color = MaterialTheme.colors.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    if (newPassState.length >= 6) {
                        pref.put(CURRENT_PWD, newPassState)
                        visibility.value = false
                        lengthError = false
                    } else {
                        lengthError = true
                    }

                },
                shape = RoundedCornerShape(12.dp),
                //colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff00c853))
            ) {
                Text("Set", modifier = Modifier.padding(all = 10.dp))
            }
        }
    }


    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}