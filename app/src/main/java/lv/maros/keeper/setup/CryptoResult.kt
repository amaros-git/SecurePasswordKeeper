package lv.maros.keeper.setup

sealed class CryptoResult {
    data class Success(val data: String) : CryptoResult()
    data class Error(val msgType: String) : CryptoResult()


    companion object {
        const val MSG_TYPE_PASSKEY_DOES_NOT_MATCH = "Wrong passkey"
        const val MSG_TYPE_ILLEGAL_PASSKEY_PROVIDED = "Passkey is empty or blank or it contains illegal symbols or is too short"
        const val MSG_TYPE_HASHING_FAILED = "Hash is null or empty"
        const val MSG_TYPE_UNKNOWN_ERROR = "Something went very wrong"
    }
}