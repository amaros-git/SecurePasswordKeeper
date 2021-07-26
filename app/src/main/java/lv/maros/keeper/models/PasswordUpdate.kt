package lv.maros.keeper.models

import androidx.room.*
import java.util.*

/**
 * For Room update query.
 */
@Entity
data class PasswordUpdate(
        @ColumnInfo(name="id") val id: Int,
        @ColumnInfo(name = "name")val name: String,
        @ColumnInfo(name = "electionDay")val electionDay: Date
)