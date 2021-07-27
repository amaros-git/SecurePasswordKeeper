package lv.maros.keeper.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "password_table")
data class Password(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "encrypted_password") val encryptedPassword: String,
    @ColumnInfo(name = "modification_date_epoch") val modificationDateEpoch: Long
)