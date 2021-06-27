package lv.maros.securedpasswordkeeper.setup

sealed class CryptoResult {
    data class Success(val msgType: String) : CryptoResult()
    data class Error(val msgType: String) : CryptoResult()


    companion object {
        const val SUCCESS_MSG_TYPE = "Success!"
        const val MISSING_PASSKEY_MSG_TYPE = "Didn't find passkey. Was it configured ?"
        const val WRONG_PASSKEY_PROVIDED_MSG_TYPE = "Wrong passkey: didn't match saved"
        const val ILLEGAL_PASSKEY_MSG_TYPE = "Passkey is empty or blank or it contains illegal symbols"
        const val HASHING_FAILED_MSG_TYPE = "Hash is null or empty"
        const val UNKNOWN_ERROR_MSG_TYPE = "Something went very wrong"
    }
}