package lv.maros.keeper.security

class KeeperPasswordGenerator(
    private val useLetters: Boolean = true,
    private val useDigits: Boolean = true,
    private val useSymbols: Boolean = true
) {

    fun generatePassword(length: Int = 16): String {
        return "ffgw3);0ddf3$$#"
    }
}