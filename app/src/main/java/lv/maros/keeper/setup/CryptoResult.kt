package lv.maros.keeper.setup

sealed class CryptoResult {
    data class Success(val msgType: String) : CryptoResult()
    data class Error(val msgType: String) : CryptoResult()


    companion object {
        const val MSG_TYPE_SUCCESS = "Success!"
        const val MSG_TYPE_MISSING_SAVED_PASSKEY = "Didn't find passkey. Was it configured ?"
        const val MSG_TYPE_WRONG_PASSKEY_PROVIDED = "Wrong passkey: didn't match saved"
        const val MSG_TYPE_ILLEGAL_PASSKEY_PROVIDED = "Passkey is empty or blank or it contains illegal symbols or is too short"
        const val MSG_TYPE_HASHING_FAILED = "Hash is null or empty"
        const val MSG_TYPE_UNKNOWN_ERROR = "Something went very wrong"
    }
}