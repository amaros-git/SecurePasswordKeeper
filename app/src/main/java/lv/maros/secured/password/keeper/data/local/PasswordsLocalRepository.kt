package lv.maros.secured.password.keeper.data.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import lv.maros.secured.password.keeper.data.PasswordDataSource
import lv.maros.secured.password.keeper.data.dto.PasswordDTO
import lv.maros.secured.password.keeper.hilt.IoDispatcher
import javax.inject.Inject

class PasswordsLocalRepository @Inject constructor(
    private val passwordDb: PasswordDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PasswordDataSource {
    override suspend fun getPassword(passwordId: Int): PasswordDTO? {
        TODO("Not yet implemented")
    }

    override suspend fun getAllPasswords(): List<PasswordDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun savePassword(password: PasswordDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePassword(newPassword: PasswordDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePassword(password: PasswordDTO) {
        TODO("Not yet implemented")
    }
}