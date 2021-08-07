package lv.maros.keeper.models

data class KeeperConfig (
    val authType: String,
    val passkeyHash: String,
    val encryptionKey: String,
    val encryptionIV: String,
    val useLogin: Boolean
)
