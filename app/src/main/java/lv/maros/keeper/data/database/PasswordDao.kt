package lv.maros.keeper.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import lv.maros.keeper.models.PasswordUpdate
import android.database.sqlite.SQLiteConstraintException
import lv.maros.keeper.models.Password

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
    suspend fun insertPassword(password: Password)


    @Update(entity = Password::class)
    suspend fun updatePassword(passwordUpdate: PasswordUpdate)

    @Query("SELECT * FROM password_table WHERE id = :passwordId")
    suspend fun getElection(passwordId: Int): Password?

    @Delete
    suspend fun deletePassword(password: Password)

}