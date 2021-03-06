package lv.maros.secured.password.keeper.data.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lv.maros.secured.password.keeper.data.PasswordDataSource
import lv.maros.secured.password.keeper.data.dto.PasswordDTO

class PasswordsLocalRepository (
    private val passwordDb: PasswordDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PasswordDataSource {
    override suspend fun getPassword(passwordId: Int): PasswordDTO? {
        return withContext(ioDispatcher) {
            passwordDb.passwordDao.getPassword(passwordId)
        }
    }

    override suspend fun getAllPasswords(): List<PasswordDTO> {
        return withContext(ioDispatcher) {
            passwordDb.passwordDao.getAllPasswords()
        }
    }

    override suspend fun savePassword(password: PasswordDTO) {
        withContext(ioDispatcher) {
            passwordDb.passwordDao.savePassword(password)
        }
    }

    override suspend fun updatePassword(password: PasswordDTO) {
        withContext(ioDispatcher) {
            passwordDb.passwordDao.updatePassword(password)
        }
    }

    override suspend fun deletePassword(passwordId: Int) {
        withContext(ioDispatcher) {
            passwordDb.passwordDao.deletePassword(passwordId)
        }
    }
}