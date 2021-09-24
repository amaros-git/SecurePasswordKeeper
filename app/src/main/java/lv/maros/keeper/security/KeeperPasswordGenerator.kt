package lv.maros.keeper.security

import java.security.SecureRandom

class KeeperPasswordGenerator(
    private val useLetters: Boolean = true,
    private val useDigits: Boolean = true,
    private val useSymbols: Boolean = true
) {

    fun generateEncryptionKey(length: Int = 16): String {
        return "kF8g8hDDXvypd5KP"
    }

    fun generateEncryptionIV(): String {
        return "fbBMmGE2Z6GtKvEs"
    }
}