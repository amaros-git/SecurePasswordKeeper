package lv.maros.securedpasswordkeeper.setup

sealed class CryptoResult {
    data class Success(val msgType: String) : CryptoResult()
    data class Error(val msgType: String) : CryptoResult()


    companion object {
        const val SUCCESS_MSG_TYPE = "Success!"
        const val MISSING_PASSKEY_MSG_TYPE = "Didn't find passkey. Was it configured ?"
        const val WRONG_PASSKEY_MSG_TYPE = "Wrong passkey"
    }
}