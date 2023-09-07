package screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Note
import navigation.NavController
import utils.*
import java.text.SimpleDateFormat
import java.util.*


var databaseStatus by mutableStateOf(DatabaseStatus.CREATE)
var currentNote by mutableStateOf(Note())

@Composable
@Preview
fun EditNoteView(
    navController: NavController
) {

    var editNoteText by remember { mutableStateOf(currentNote.noteText) }
    var currentDate by remember { mutableStateOf(currentNote.date) }
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    var mNoteColor by remember { mutableStateOf(currentNote.noteColor) }
    var mFontWeight by remember { mutableStateOf(currentNote.fontStyle) }
    var mFontSize by remember { mutableStateOf(currentNote.fontSize) }
    var mFontFamily by remember { mutableStateOf(currentNote.fontsFamily) }
    var mItalicFont by remember { mutableStateOf(currentNote.italicFont) }
    var isExpendedFontStyleMenu by remember { mutableStateOf(false) }
    var isExpendedFontMenu by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(getSelectedItem(mNoteColor)) }



    Column(modifier = Modifier.fillMaxSize().background(color = applyNoteColor(mNoteColor).first)) {


        Row(
            modifier = Modifier.fillMaxWidth().height(42.dp)
                .background(color = Color(255, 255, 238, 80)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.height(4.dp).width(4.dp))
            IconButton(
                onClick = {
                    navController.navigateBack()
                },
                modifier = Modifier.size(36.dp).padding(all = 4.dp)
            ) {
                Image(imageVector = Icons.Default.ArrowBack, contentDescription = null, alpha = 0.5f)
            }

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(end = 16.dp)
            ) {

                val superscript = SpanStyle(
                    baselineShift = BaselineShift.Superscript,
                    fontSize = 16.sp, // font size of superscript
                )

                TextButton(onClick = {
                    mFontSize -= 2
                    mUpdateNote(
                        fontSize = mFontSize,
                        fontWeight = mFontWeight,
                        fontFamily = mFontFamily,
                        italicFont = mItalicFont,
                        noteColor = mNoteColor,
                        date = currentDate,
                        noteText = editNoteText
                    )
                }) {
                    Text(
                        text = buildAnnotatedString {
                            append("A")
                            withStyle(superscript) {
                                append("-")
                            }
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = CustomFonts.roboto_slab_10
                    )
                }

                TextButton(onClick = {
                    mFontSize += 2
                    mUpdateNote(
                        fontSize = mFontSize,
                        fontWeight = mFontWeight,
                        fontFamily = mFontFamily,
                        italicFont = mItalicFont,
                        noteColor = mNoteColor,
                        date = currentDate,
                        noteText = editNoteText
                    )
                }) {
                    Text(
                        text = buildAnnotatedString {
                            append("A")
                            withStyle(superscript) {
                                append("+")
                            }
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = CustomFonts.roboto_slab_10
                    )
                }


                Box {
                    TextButton(onClick = {
                        isExpendedFontMenu = true
                    }) {
                        Text(
                            text = buildAnnotatedString {
                                append("A")
                                withStyle(superscript) {
                                    append("A")
                                }
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light,
                            fontFamily = CustomFonts.roboto_slab_10
                        )
                    }

                    DropdownMenu(
                        expanded = isExpendedFontMenu,
                        onDismissRequest = { isExpendedFontMenu = false }
                    ) {


                        FontsEnum.values().forEach {

                            DropdownMenuItem(onClick = {
                                mFontFamily = it.name
                                mUpdateNote(
                                    mNoteColor,
                                    mFontSize,
                                    mFontWeight,
                                    mFontFamily,
                                    mItalicFont,
                                    currentDate,
                                    editNoteText
                                )

                                isExpendedFontMenu = false
                            }) {
                                Text(
                                    it.name.lowercase(),
                                    fontWeight = FontWeight.Light,
                                    fontFamily = applyFontFamily(it.name)
                                )

                            }
                        }
                    }
                }


                TextButton(onClick = {
                    mItalicFont = !mItalicFont
                    mUpdateNote(
                        fontSize = mFontSize,
                        fontWeight = mFontWeight,
                        fontFamily = mFontFamily,
                        italicFont = mItalicFont,
                        noteColor = mNoteColor,
                        date = currentDate,
                        noteText = editNoteText
                    )

                }) {
                    Text(
                        text = "I",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = if (mItalicFont) FontWeight.Bold else FontWeight.Light,
                            fontStyle = if (mItalicFont) FontStyle.Italic else FontStyle.Normal,
                            fontFamily = CustomFonts.roboto_slab_10
                        )
                    )
                }

                Box {
                    TextButton(onClick = {
//                    mFontWeight = !mFontWeight
//                    mUpdateNote()
                        isExpendedFontStyleMenu = true

                    }) {

                        Text(
                            text = "B",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Light,
                                fontFamily = CustomFonts.roboto_slab_10
                            )
                        )
                    }


                    DropdownMenu(
                        expanded = isExpendedFontStyleMenu,
                        onDismissRequest = { isExpendedFontStyleMenu = false }
                    ) {

                        FontWeightsEnum.values().forEach {
                            DropdownMenuItem(onClick = {
                                mFontWeight = it.name
                                mUpdateNote(
                                    fontSize = mFontSize,
                                    fontWeight = mFontWeight,
                                    fontFamily = mFontFamily,
                                    italicFont = mItalicFont,
                                    noteColor = mNoteColor,
                                    date = currentDate,
                                    noteText = editNoteText
                                )

                                isExpendedFontStyleMenu = false
                            }) {
                                Text(it.name, fontWeight = applyFontWeights(it.name))

                            }
                        }

                    }
                }



                Spacer(modifier = Modifier.width(16.dp))


                RadioOptions.values().forEach {

                    CustomRadioButton(
                        isSelected = selectedOption == it,
                        color = getNoteColor(it.name).second,
                        onClick = {
                            selectedOption = it
                            mNoteColor = getNoteColor(it.name).first
                            mUpdateNote(
                                fontSize = mFontSize,
                                fontWeight = mFontWeight,
                                fontFamily = mFontFamily,
                                italicFont = mItalicFont,
                                noteColor = mNoteColor,
                                date = currentDate,
                                noteText = editNoteText
                            )
                        },

                        )
                }


            }


        }

        Column(modifier = Modifier.fillMaxWidth().weight(1f)) {

            TextField(value = editNoteText, onValueChange = {

                editNoteText = it
                if (databaseStatus == DatabaseStatus.CREATE) {
                    currentDate = SimpleDateFormat("dd/MM/yyyy_hh:mm:ss:SSS", Locale.US).format(Date())

                    currentNote = Note().apply {
                        this.noteColor = mNoteColor
                        this.fontSize = mFontSize
                        this.fontStyle = mFontWeight
                        this.italicFont = mItalicFont
                        this.date = currentDate
                        this.fontsFamily = mFontFamily
                        this.noteText = editNoteText
                    }
                    insertNote(currentNote)
                    databaseStatus = DatabaseStatus.UPDATE
                } else {
                    mUpdateNote(
                        fontSize = mFontSize,
                        fontWeight = mFontWeight,
                        fontFamily = mFontFamily,
                        italicFont = mItalicFont,
                        noteColor = mNoteColor,
                        date = currentDate,
                        noteText = editNoteText
                    )
                }

            },
                textStyle = TextStyle(
                    fontSize = mFontSize.sp,
                    fontWeight = applyFontWeights(mFontWeight),
                    fontFamily = applyFontFamily(mFontFamily),
                    fontStyle = if (mItalicFont) FontStyle.Italic else FontStyle.Normal
                ),
                modifier = Modifier.padding(start = 8.dp, bottom = 16.dp, end = 8.dp)
                    .fillMaxSize()
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = applyNoteColor(mNoteColor).second,
                    disabledTextColor = Color.Gray,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                placeholder = { Text("Write note....") }
            )
        }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }


    }


}

enum class RadioOptions { OptionDefault, Option1, Option2, Option3, Option4, Option5, Option6 }


fun mUpdateNote(
    noteColor: String = currentNote.noteColor,
    fontSize: Int = currentNote.fontSize,
    fontWeight: String = currentNote.fontStyle,
    fontFamily: String = currentNote.fontsFamily,
    italicFont: Boolean = false,
    date: String = currentNote.date,
    noteText: String = currentNote.noteText
) {
    currentNote = Note().apply {

        this.noteColor = noteColor
        this.fontSize = fontSize
        this.fontStyle = fontWeight
        this.fontsFamily = fontFamily
        this.italicFont = italicFont
        this.date = date
        this.noteText = noteText
    }
    updateNote(currentNote)
}


