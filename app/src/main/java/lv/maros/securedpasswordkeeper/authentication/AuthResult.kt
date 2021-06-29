package lv.maros.securedpasswordkeeper.authentication

sealed class AuthResult {
    data class Success(val msgType: String) : AuthResult()
    data class Error(val msgType: String) : AuthResult()
}