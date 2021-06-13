package lv.maros.securedpasswordkeeper.authentication

sealed class AuthResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : AuthResult<T>()
    data class Fail(val message: String) : AuthResult<Nothing>()
    data class Error(val message: String) : AuthResult<Nothing>()
}