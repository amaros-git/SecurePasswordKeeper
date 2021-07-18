package lv.maros.keeper.utils

/**
 * General purpose class.
 */
sealed class KeeperResult {
    data class Success(val msgType: String) : KeeperResult()
    data class Error(val msgType: String) : KeeperResult()
}