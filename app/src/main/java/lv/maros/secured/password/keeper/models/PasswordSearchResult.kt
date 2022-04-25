package lv.maros.secured.password.keeper.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PasswordSearchResult(
    val result: String,
    val type: String,
    val id: Int
) : Parcelable {
    companion object {
        const val TYPE_WEBSITE = "website"
        const val TYPE_USERNAME = "username"
    }
}