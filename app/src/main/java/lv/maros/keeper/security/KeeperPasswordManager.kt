package lv.maros.keeper.security

import androidx.annotation.Keep
import lv.maros.keeper.utils.KeeperResult


object KeeperPasswordManager {

    private var useLetters: Boolean = true
    private var useDigits: Boolean = true
    private var useSymbols: Boolean = true

    fun generateEncryptionKey(length: Int = 16): String {
        return "kF8g8hDDXvypd5KP"
    }

    fun generateEncryptionIV(): String {
        return "fbBMmGE2Z6GtKvEs"
    }

    fun verifyPassword(password: String): KeeperResult<String> {
        return when {
            password.length < PASSWORD_MIN_LENGTH -> {
                KeeperResult.Error(PASSWORD_TOO_SHORT)
            }

            password.isBlank() -> {
                KeeperResult.Error(PASSWORD_IS_BLANK)
            }

            //TODO check if contains any space or whitespace

            else -> KeeperResult.Success(PASSWORD_IS_VALID)
        }
    }


    const val PASSWORD_MIN_LENGTH = 4 //TODO CHANGE For production ) 4 just to input faster

    const val PASSWORD_TOO_SHORT = "Password too short"
    const val PASSWORD_IS_BLANK = "Password is blank"
    const val PASSWORD_IS_VALID = "Password is valid"
}