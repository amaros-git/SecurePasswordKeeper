package lv.maros.secured.password.keeper.data.local

import androidx.room.*
import lv.maros.secured.password.keeper.data.dto.PasswordDTO

@Dao
interface - {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun savePassword(password: PasswordDTO)

    @Query("SELECT * FROM password_table")
    suspend fun getAllPasswords(): List<PasswordDTO>

    @Update(entity = PasswordDTO::class)
    suspend fun updatePassword(newPassword: PasswordDTO)

    @Query("SELECT * FROM password_table WHERE password_id = :passwordId")
    suspend fun getPassword(passwordId: Int): PasswordDTO?

    @Query("DELETE FROM password_table WHERE password_id = :passwordId")
    suspend fun deletePassword(passwordId: Int)
}