package lv.maros.secured.password.keeper.data

import lv.maros.secured.password.keeper.data.dto.PasswordDTO

interface PasswordDataSource {
    suspend fun getPassword(passwordId: Int): PasswordDTO?

    suspend fun getAllPasswords(): List<PasswordDTO>

    suspend fun savePassword(password: PasswordDTO)

    suspend fun updatePassword(password: PasswordDTO)

    suspend fun deletePassword(passwordId: Int)
}