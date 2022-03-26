package lv.maros.secured.password.keeper.security

import kotlin.random.Random

object KeeperPasswordManager {

    private val allowedLetters = ('A'..'Z') + ('a'..'z')
    private val allowedDigits = ('0'..'9').toList()
    private val allowedSymbols = """
        ~!@#$%^&*()_+"|<>?
    """.trimIndent().toList()

    private var useLetters: Boolean = true
    private var useDigits: Boolean = true
    private var useSymbols: Boolean = true

    private fun getRandomSymbol(): Char {
        val i = Random.nextInt(0, allowedSymbols.length)
        return allowedSymbols[i]
    }

    private fun getAllowedCharsRange(): CharRange {
        val range = CharRange.EMPTY
        when {
            useLetters -> {
                range + allowedLetters
            }
            useDigits -> {
                range + allowedDigits
            }
        }

        return range
    }

    fun generateEncryptionKey(length: Int = 16): String {
        return "kF8g8hDDXvypd5KP"
    }

    fun generateEncryptionIV(): String {
        return "fbBMmGE2Z6GtKvEs"
    }

    fun generatePassword(length: Int): String {
        val chars = getAllowedCharsRange()
        return (1 .. length).map { chars.random() }.joinToString("")
    }
}