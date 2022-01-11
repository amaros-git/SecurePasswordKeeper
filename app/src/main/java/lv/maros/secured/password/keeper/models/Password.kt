package lv.maros.keeper.models


data class Password(
   val website: String,
   val username: String,
   val encryptedPassword: String,
   val passwordLastModificationDate: Long,
   val id: Int //when password is created in UI, set to 0
)