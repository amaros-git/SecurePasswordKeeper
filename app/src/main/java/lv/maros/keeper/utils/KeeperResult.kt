package lv.maros.keeper.utils

/**
 * General purpose class.
 */
sealed class KeeperResult {
    data class Success(val value: String) : KeeperResult()
    data class Error(val value: String) : KeeperResult()
}