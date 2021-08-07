package lv.maros.keeper.security

class KeeperPasswordGenerator(
    private val useLetters: Boolean = true,
    private val useDigits: Boolean = true,
    private val useSymbols: Boolean = true
) {

    fun generatePassword(length: Int = 16): String {
        return "1234567890123456"
    }

    fun generateIV(): String {
        return "1234567890123456"
    }
}