package lv.maros.secured.password.keeper.models

data class PasswordSearchResultItem(
    val result: String,
    val type: String,
    val id: Int
) {
    companion object {
        const val TYPE_WEBSITE = "website"
        const val TYPE_USERNAME = "username"
    }
}