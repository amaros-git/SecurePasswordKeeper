package lv.maros.secured.password.keeper.models

/**
 * I use this data class to store data entered in UI and verify them before I save them in database
 */
data class PasswordInputData (
    val website: String,
    val username: String,
    val password: String,
    val repeatPassword: String
)
