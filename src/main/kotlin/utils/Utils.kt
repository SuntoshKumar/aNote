package utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import data.Note
import screens.RadioOptions
import java.awt.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.swing.JFileChooser


fun loadSvgPainter(file: File, density: Density): Painter =
    file.inputStream().buffered().use { loadSvgPainter(it, density) }

fun loadImageBitmaps(file: File): ImageBitmap =
    file.inputStream().buffered().use(::loadImageBitmap)


fun applyFontFamily(fontName:String): FontFamily {
    return when(fontName){
        FontsEnum.ROBOTO.name -> CustomFonts.roboto_1
        FontsEnum.BARLO.name -> CustomFonts.barlo_2
        FontsEnum.BOREL.name -> CustomFonts.borel_3
        FontsEnum.ROBOTO_CONDENSED.name -> CustomFonts.robotoCondensed_4
        FontsEnum.UBUNTU.name -> CustomFonts.ubuntu_5
        FontsEnum.COMFORTAA.name -> CustomFonts.comfortaa_6
        FontsEnum.COUSINE.name -> CustomFonts.cousine_7
        FontsEnum.PLEX_SERIF.name -> CustomFonts.ibmPlexSerif_8
        FontsEnum.INCONSOLATA.name -> CustomFonts.inconsolata_9
        FontsEnum.ROBOTO_SLAB.name -> CustomFonts.roboto_slab_10
        FontsEnum.SOFIA.name -> CustomFonts.sofia_11
        FontsEnum.SACRAMENTO.name -> CustomFonts.sacramento_12
        else -> { FontFamily.Default}
    }
}

fun saveAsText(note:Note){
    val result = getSelectedDirectory()

    try {
        val file = File("$result/note.txt")
        val out = file.outputStream()
        out.write(note.noteText.toByteArray())
        out.close()
    }catch (e:Exception){
        e.printStackTrace()
    }

}

fun saveSingleNoteAsPdf(note: Note){
    val doc = Document()

    val result = getSelectedDirectory()

    try {
        PdfWriter.getInstance(doc, FileOutputStream("$result/note.pdf"))
        doc.open()

        //doc.addAuthor("harikesh")
        doc.add(Paragraph(note.noteText))
        doc.close()
//        Dialog(onCloseRequest = {}, title = "Error"){
//            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
//                Text("Failed to save file!")
//            }
//        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

fun getSelectedItem(noteColor: String): RadioOptions {
    return when (noteColor) {
        NoteColor.PINK.name -> {
            RadioOptions.Option1
        }

        NoteColor.PURPLE.name -> {
            RadioOptions.Option2
        }

        NoteColor.GREEN.name -> {
            RadioOptions.Option3
        }

        NoteColor.YELLOW.name -> {
            RadioOptions.Option4
        }

        NoteColor.GRAY.name -> {
            RadioOptions.Option5
        }

        NoteColor.BLUE.name -> {
            RadioOptions.Option6
        }

        else -> {
            RadioOptions.OptionDefault
        }
    }
}

fun applyFontWeights(weight: String): FontWeight {
    return when (weight) {

        FontWeightsEnum.LIGHT.name -> {
            FontWeight.Light
        }

        FontWeightsEnum.BOLD.name -> {
            FontWeight.Bold
        }

        else -> {
            FontWeight.Normal
        }
    }
}

fun getNoteColor(options: String): Pair<String, Color> {
    return when (options) {

        RadioOptions.Option1.name -> {
            Pair(NoteColor.PINK.name, noteColor_1.first)

        }

        RadioOptions.Option2.name -> {
            Pair(NoteColor.PURPLE.name, noteColor_2.first)
        }

        RadioOptions.Option3.name -> {
            Pair(NoteColor.GREEN.name, noteColor_3.first)
        }

        RadioOptions.Option4.name -> {
            Pair(NoteColor.YELLOW.name, noteColor_4.first)
        }

        RadioOptions.Option5.name -> {
            Pair(NoteColor.GRAY.name, noteColor_5.first)
        }

        RadioOptions.Option6.name -> {
            Pair(NoteColor.BLUE.name, noteColor_6.first)
        }

        else -> {
            Pair(NoteColor.DEFAULT.name, noteColor_default.first)
        }

    }
}

fun applyNoteColor(name: String): Pair<Color, Color> {
    return when (name) {
        NoteColor.PINK.name -> {
            noteColor_1
        }

        NoteColor.PURPLE.name -> {
            noteColor_2
        }

        NoteColor.GREEN.name -> {
            noteColor_3
        }

        NoteColor.YELLOW.name -> {
            noteColor_4
        }

        NoteColor.GRAY.name -> {
            noteColor_5
        }

        NoteColor.BLUE.name -> {
            noteColor_6
        }

        else -> {
            noteColor_default
        }
    }
}

@Throws(AWTException::class)
fun displayTray() {
    //Obtain only one instance of the SystemTray object
    val tray = SystemTray.getSystemTray()

    //If the icon is a file
    val image: Image = Toolkit.getDefaultToolkit().createImage("icon.png")
    //Alternative (if the icon is on the classpath):
    //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));
    val trayIcon = TrayIcon(image, "Tray Demo")
    //Let the system resize the image if needed
    trayIcon.setImageAutoSize(true)
    //Set tooltip text for the tray icon
    trayIcon.setToolTip("System tray icon demo")
    tray.add(trayIcon)
    trayIcon.displayMessage("Hello, World", "notification demo", TrayIcon.MessageType.INFO)
}

/***
 * This function is to export all note as a zip file ***/
fun exoportNoteAsZipFile() {

    val result = getSelectedDirectory()

    val noteList = mutableListOf<File>()

    val date = SimpleDateFormat("dd_MM_yyyy_SSS").format(Date())
    val fos = FileOutputStream("${result?.absolutePath}/aNote_${date}.zip")
    val zos = ZipOutputStream(fos)

    try {

        for (note in getAllNoteList()){
            val file = File("$result/Note_${note.date.replace("/","_")}.txt")
            val out = file.outputStream()
            out.write(note.noteText.toByteArray())
            out.close()
            noteList.add(file)
        }

        for (file in noteList) {
            val entry = ZipEntry(file.name)
            zos.putNextEntry(entry)

            val buffer = ByteArray(1024)
            var len: Int
            val fis = file.inputStream()

            while (fis.read(buffer).also { len = it } > 0) {
                zos.write(buffer, 0, len)
            }

            fis.close()
            zos.closeEntry()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        zos.close()
        fos.close()

        for (n in noteList){
            try {
                n.delete()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}

fun unzip(zipFilePath: File) : File{


    val buffer = ByteArray(1024)
    val outputDir = File("${zipFilePath.parentFile.absolutePath}/Restored")

    try {
        // Create the output directory if it doesn't exist

        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        // Create a ZipInputStream to read the zip file
        val zis = ZipInputStream(FileInputStream(zipFilePath.absolutePath))
        var zipEntry = zis.nextEntry

        while (zipEntry != null) {
            val entryFileName = zipEntry.name
            val entryFile = File(outputDir, entryFileName)

            // Create directories as needed
            if (zipEntry.isDirectory) {
                entryFile.mkdirs()
            } else {
                // Create parent directories for the file
                val parentDir = entryFile.parentFile
                if (!parentDir.exists()) {
                    parentDir.mkdirs()
                }

                // Write the file content
                val fos = FileOutputStream(entryFile)
                var len: Int
                while (zis.read(buffer).also { len = it } > 0) {
                    fos.write(buffer, 0, len)
                }
                fos.close()
            }

            zis.closeEntry()
            zipEntry = zis.nextEntry
        }

        zis.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return outputDir
}

fun getSelectedDirectory():File?{
    val fC: JFileChooser = JFileChooser("/").apply {
        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        dialogTitle = "Select a folder"
        approveButtonText = "Select"
        approveButtonToolTipText = "Select current directory as save destination"
    }
    fC.showOpenDialog(null /* OR null */)
    return fC.selectedFile
}
/* Loading from file with java.io API */


//fun loadSvgPainter(file: File, density: Density): Painter =
//    file.inputStream().buffered().use { loadSvgPainter(it, density) }

//fun loadXmlImageVector(file: File, density: Density): ImageVector =
//    file.inputStream().buffered().use { loadXmlImageVector(InputSource(it), density) }

/* Loading from network with java.net API */

//fun loadImageBitmap(url: String): ImageBitmap =
//    URL(url).openStream().buffered().use(::loadImageBitmap)
//
//fun loadSvgPainter(url: String, density: Density): Painter =
//    URL(url).openStream().buffered().use { loadSvgPainter(it, density) }
//
//fun loadXmlImageVector(url: String, density: Density): ImageVector =
//    URL(url).openStream().buffered().use { loadXmlImageVector(InputSource(it), density) }

/* Loading from network with Ktor client API (https://ktor.io/docs/client.html). */
