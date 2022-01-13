package lv.maros.secured.password.keeper.utils

/**
 * General purpose class.
 */
sealed class KeeperResult<out T: Any> {
    data class Success<out T: Any>(val data: T) : KeeperResult<T>()
    data class Error(val value: String) : KeeperResult<Nothing>()
}