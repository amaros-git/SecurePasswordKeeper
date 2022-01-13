package lv.maros.secured.password.keeper.data

import lv.maros.secured.password.keeper.data.dto.PasswordDTO

interface PasswordDataSource {
    suspend fun getPassword(passwordId: Int): PasswordDTO?

    suspend fun getAllPasswords(): List<PasswordDTO>

    suspend fun savePassword(password: PasswordDTO)

    suspend fun updatePassword(newPassword: PasswordDTO)

    suspend fun deletePassword(password: PasswordDTO)
}