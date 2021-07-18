package lv.maros.keeper.models

data class Password(
    val id: Int,
    val description: String,
    val url: String?,
    val username: String,
    val encryptedPassword: String,
    val creationDateEpoch: Long
)