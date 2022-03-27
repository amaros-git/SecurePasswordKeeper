package lv.maros.secured.password.keeper.security

import kotlin.random.Random

object KeeperPasswordManager {

    private val uppercaseLetters = ('A' .. 'Z')
    private val lowercaseLetters = ('a' .. 'z')
    private val allowedLetters = uppercaseLetters + lowercaseLetters
    private val allowedDigits = ('0'..'9').toList()
    private val allowedSymbols = """
        !@#$%^&*()_+=
    """.trimIndent().toList()

    //Default config
    private var useLetters: Boolean = true
    private var useDigits: Boolean = true
    private var useSymbols: Boolean = true


    private fun getAllowedCharList(): List<Char> {
        val charList = mutableListOf<Char>()
        if (useLetters) {
            charList += allowedLetters
        }
        if (useDigits) {
            charList += allowedDigits
        }
        if (useSymbols) {
            charList += allowedSymbols
        }

        return charList.toList()
    }

    private fun searchForCharInString(string: String, chars: List<Char>): Boolean {
        chars.forEach {
            if(string.contains(it, false)) {
                return true
            }
        }
        return false
    }

    fun isContainingUppercaseLetter(password: String): Boolean {
        return searchForCharInString(password, uppercaseLetters.toList())
    }

    fun isContainingDigits(password: String): Boolean {
        return searchForCharInString(password, allowedDigits)
    }

    fun isContainingSymbols(password: String): Boolean {
        return searchForCharInString(password, allowedSymbols)
    }



    //TODO
    fun generateEncryptionKey(length: Int = 16): String {
        return "kF8g8hDDXvypd5KP"
    }

    //TODO
    fun generateEncryptionIV(length: Int = 16): String {
        return "fbBMmGE2Z6GtKvEs"
    }

    fun generatePassword(length: Int): String {
        val chars = getAllowedCharList()
        return (1 .. length).map { chars.random() }.joinToString("")
    }
}