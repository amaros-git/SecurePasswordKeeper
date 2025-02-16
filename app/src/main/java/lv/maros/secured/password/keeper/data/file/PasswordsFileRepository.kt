package lv.maros.secured.password.keeper.data.file

import android.app.Application
import android.content.Context
import androidx.work.impl.close
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lv.maros.secured.password.keeper.data.PasswordDataSource
import lv.maros.secured.password.keeper.models.PasswordDTO
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

//TODO not implemented
class PasswordFileRepository (
    app: Application,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PasswordDataSource {
    override suspend fun getPassword(passwordId: Int): PasswordDTO {
        return withContext(ioDispatcher) {
            getDummyPasswordsDTO()
        }
    }

    override suspend fun getAllPasswords(): List<PasswordDTO> {
        return withContext(ioDispatcher) {
            emptyList()
        }
    }

    override suspend fun savePassword(password: PasswordDTO) {
        withContext(ioDispatcher) {
            //TODO: Implement
        }
    }

    override suspend fun updatePassword(password: PasswordDTO) {
        withContext(ioDispatcher) {
            //TODO: Implement
        }
    }

    override suspend fun deletePassword(passwordId: Int) {
        withContext(ioDispatcher) {
            //TODO: Implement
        }
    }

    private fun readTextFromAsset(context: Context, fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String? = reader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                stringBuilder.append("\n") // TODO Add newline if needed ?
                line = reader.readLine()
            }
            reader.close()
            stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}

fun getDummyPasswordsDTO() =
    PasswordDTO(
        username = "username",
        website = "website",
        encryptedPassword = "password",
        passwordLastModificationDate = 1739810700000L,
        id = 1
    )