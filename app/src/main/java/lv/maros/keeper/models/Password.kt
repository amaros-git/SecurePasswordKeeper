package lv.maros.keeper.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Password(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "encrypted_password") val encryptedPassword: String,
    @ColumnInfo(name = "creation_date_epoch") val creationDateEpoch: Long
)