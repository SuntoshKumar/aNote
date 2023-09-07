package utils

import data.Note
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.swing.JFileChooser
import javax.swing.text.Document
import javax.swing.text.rtf.RTFEditorKit


/***
 * To backup all note to disk ***/
fun backup() {

    val selectedResult = getSelectedDirectory()

    if (selectedResult != null) {
        val noteList = mutableListOf<File>()

        val date = SimpleDateFormat("dd_MM_yyyy_SSS").format(Date())
        try {
            val fileToSave = File("${selectedResult.absolutePath}/aNote/Backup")
            if (!fileToSave.exists()) {
                fileToSave.mkdirs()
            }

            val fos = FileOutputStream("$fileToSave/aNote_${date}.an")

            val zos = ZipOutputStream(fos)

            try {

                for (note in getAllNoteList()) {
                    val file = File("$selectedResult/Note_${note.date.replace("/", "_")}.txt")

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

                for (n in noteList) {
                    try {
                        n.delete()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


}

/***
 * To restore backup file***/
fun restore() {

    val fC: JFileChooser = JFileChooser("/").apply {
        fileSelectionMode = JFileChooser.FILES_ONLY
        dialogTitle = "Select backup file"
        approveButtonText = "Select"
        approveButtonToolTipText = "Select your backup .an file to restore"
    }
    fC.showOpenDialog(null /* OR null */)
    val result: File? = fC.selectedFile


    val unzipFilePath = result?.let { unzip(it) }

    unzipFilePath?.listFiles()?.forEach { file ->

        val stringBuilder = StringBuilder()
        val date = SimpleDateFormat("dd/MM/yyyy_hh:mm:ss:SSS").format(Date())
        try {

            if (file.exists()) {
                val reader = BufferedReader(FileReader(file))
                var line: String?

                // Read lines until the end of the file
                while (reader.readLine().also { line = it } != null) {
                    // Process each line here
                    stringBuilder.append(line)
                }

                reader.close()

                val note = Note().apply {
                    this.date = date
                    this.noteText = stringBuilder.toString()
                }

                insertNote(note)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            stringBuilder.clear()
            file.delete()
        }

    }


}

fun importNoteFromTextFile() {

    val fC: JFileChooser = JFileChooser("/").apply {
        fileSelectionMode = JFileChooser.FILES_ONLY
        dialogTitle = "Select a file"
        approveButtonText = "Select"
        approveButtonToolTipText = "Select file to import as note"
    }
    fC.showOpenDialog(null /* OR null */)
    val txtFile: File? = fC.selectedFile

    if (txtFile?.exists() == true) {

        val date = SimpleDateFormat("dd/MM/yyyy_hh:mm:ss:SSS").format(Date())

        if (txtFile.extension == "rtf") {
            val rtfParser = RTFEditorKit()
            val document: Document = rtfParser.createDefaultDocument()
            rtfParser.read(ByteArrayInputStream(txtFile.readBytes()), document, 0)
            val text: String = document.getText(0, document.length)
            val note = Note().apply {
                this.date = date
                this.noteText = text
            }

            insertNote(note)
        } else {
            val stringBuilder = StringBuilder()

                val reader = BufferedReader(FileReader(txtFile))
                var line: String?

                // Read lines until the end of the file
                while (reader.readLine().also { line = it } != null) {
                    // Process each line here
                    stringBuilder.append(line)
                }

                reader.close()

                val note = Note().apply {
                    this.date = date
                    this.noteText = stringBuilder.toString()
                }

                insertNote(note)
            }


    }


}