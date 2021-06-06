package lv.maros.securedpasswordkeeper

import java.util.*

data class Password(
    val id: Int,
    val description: String,
    val url: String?,
    val username: String,
    val password: String,
    val creationDateEpoch: Long
)