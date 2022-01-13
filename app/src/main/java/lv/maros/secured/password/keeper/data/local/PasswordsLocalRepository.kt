package lv.maros.secured.password.keeper.data.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class PasswordsLocalRepository(
    private val passwordDb: PasswordDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
}