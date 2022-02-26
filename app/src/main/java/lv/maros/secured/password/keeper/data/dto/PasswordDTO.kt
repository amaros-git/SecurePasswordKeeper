package lv.maros.secured.password.keeper.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Immutable model class for a Password. In order to compile with Room
 *
 * @param website                       URL or the name of website, e.g. Google, Amazon, etc.
 * @param username                      username
 * @param encryptedPassword             encrypted password
 * @param passwordLastModificationDate  we care only about encryptedPassword field. E.g. to remind user change a password.
 * @param id                            id of the password. Room increments it internally.
 */

@Entity(tableName = "password_table")
data class PasswordDTO(
    @ColumnInfo(name = "website") val website: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "encrypted_password") val encryptedPassword: String,
    @ColumnInfo(name = "password_last_modification_date") val passwordLastModificationDate: Long,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "password_id") var id: Int
)