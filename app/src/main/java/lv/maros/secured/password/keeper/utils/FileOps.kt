package lv.maros.secured.password.keeper.utils

import android.content.Context
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

object FileOps {

    private const val TAG = "FileOps"

    /**
     * Writes the given data to a file in the app's internal storage.
     *
     * @param context The application context.
     * @param fileName The name of the file to write to.
     * @param data The data to write to the file.
     * @return True if the write was successful, false otherwise.
     */
    fun writeToFile(context: Context, fileName: String, data: String): Boolean {
        val file = File(context.filesDir, fileName)
        return try {
            FileOutputStream(file).bufferedWriter().use { writer ->
                writer.write(data)
            }
            true
        } catch (e: IOException) {
            Timber.e("Error writing to file: $fileName")
            false
        }
    }

    /**
     * Reads the content of a file from the app's internal storage.
     *
     * @param context The application context.
     * @param fileName The name of the file to read from.
     * @return The content of the file as a String, or null if an error occurred or the file does not exist.
     */
    fun readFromFile(context: Context, fileName: String): String? {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) {
            Timber.w("File not found: $fileName")
            return null
        }
        return try {
            FileInputStream(file).bufferedReader().use { reader ->
                reader.readText()
            }
        } catch (e: IOException) {
            Timber.w("Error reading from file: $fileName")
            null
        }
    }

    /**
     * Prints the names of all files in the app's internal storage to the console.
     *
     * @param context The application context.
     */
    fun printListOfFiles(context: Context) {
        val filesDir = context.filesDir
        val fileNames = filesDir.listFiles()?.map { it.name } ?: emptyList()
        if (fileNames.isEmpty()) {
            Timber.e("No files found in internal storage.")
        } else {
            println("Files in internal storage:")
            fileNames.forEach(::println)
        }
    }
}