package lv.maros.keeper.data.local

import androidx.room.*
import android.database.sqlite.SQLiteConstraintException
import lv.maros.keeper.data.dto.PasswordDTO

@Dao
interface PasswordDao {

    /**
     * Insert a election in the database. If the election already exists, replace it.
     *
     * @param election the election to be inserted.
     *
     * @throws SQLiteConstraintException is there is election with the same id. Use update
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPassword(password: PasswordDTO)

    @Query("SELECT * FROM password_table")
    suspend fun getAllPasswords(): List<PasswordDTO>

    @Update(entity = PasswordDTO::class)
    suspend fun updatePassword(newPassword: PasswordDTO)

    @Query("SELECT * FROM password_table WHERE password_id = :passwordId")
    suspend fun getElection(passwordId: Int): PasswordDTO?

    @Delete
    suspend fun deletePassword(password: PasswordDTO)

}