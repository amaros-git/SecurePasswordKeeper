package lv.maros.secured.password.keeper.models

data class KeeperConfig (
    var authType: String?,
    var passkeyHash: String?,
    var encryptionKey: String?,
    var encryptionIV: String?,
    var useLogin: Boolean
)
