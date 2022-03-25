package lv.maros.secured.password.keeper.security

import timber.log.Timber
import kotlin.random.Random

object KeeperPasswordManager {

    private val allowedLetters = ('A'..'Z') + ('a'..'z')
    private val allowedNumbers = ('0'..'9')
    private val allowedSymbols = """
        ~!@#$%^&*()_+"|<>?
    """.trimIndent()

    private var useLetters: Boolean = true
    private var useDigits: Boolean = true
    private var useSymbols: Boolean = true

    private fun getRandomSymbol(): Char {
        val i = Random.nextInt(0, allowedSymbols.length)
        return allowedSymbols[i]
    }

    fun generateEncryptionKey(length: Int = 16): String {
        return "kF8g8hDDXvypd5KP"
    }

    fun generateEncryptionIV(): String {
        return "fbBMmGE2Z6GtKvEs"
    }

    fun generatePassword(length: Int): String {
        val char = getRandomSymbol()
        /*val allowedChars = ('A'..'Z') +  +

        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")*/
        return ""
    }
}