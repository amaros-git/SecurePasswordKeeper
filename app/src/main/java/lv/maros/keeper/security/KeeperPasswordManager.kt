package lv.maros.keeper.security

import androidx.annotation.Keep
import lv.maros.keeper.utils.KeeperResult


object KeeperPasswordManager {

    private var useLetters: Boolean = true
    private var useDigits: Boolean = true
    private var useSymbols: Boolean = true

    fun generateEncryptionKey(length: Int = 16): String {
        return "kF8g8hDDXvypd5KP"
    }

    fun generateEncryptionIV(): String {
        return "fbBMmGE2Z6GtKvEs"
    }

    fun verifyPassword(password: String): KeeperResult<String> {
        return when {
            password.length < PASSWORD_MIN_LENGTH -> {
                KeeperResult.Error(PASSWORD_TOO_SHORT)
            }

            password.isBlank() -> {
                KeeperResult.Error(PASSWORD_IS_BLANK)
            }

            //TODO check if contains any space or whitespace
           /*password.isBlank() -> {
                binding.passwordLayout.error =
                    requireContext().getString(R.string.password_empty_error)
                false
            }
            password.length < KEEPER_PASSWORD_MIN_LENGTH -> {
                binding.passwordLayout.error =
                    requireContext().getString(R.string.password_min_len_error)
                false
            }
            password.contains(" ", true) -> {
                binding.passwordLayout.error = getString(R.string.space_char_error)
                false
            }*/

            /*

            repeatPassword.isBlank() -> {
                binding.repeatPasswordLayout.error =
                    requireContext().getString(R.string.password_empty_error)
                false
            }
            repeatPassword.length < PASSWORD_MIN_LENGTH -> {
                binding.repeatPasswordLayout.error =
                    requireContext().getString(R.string.password_min_len_error)
                false
            }
            repeatPassword.contains(" ", true) -> {
                binding.repeatPasswordLayout.error = getString(R.string.space_char_error)
                false
            }

            password != repeatPassword -> {
                binding.passwordLayout.error = getString(R.string.password_dont_match_error)
                binding.repeatPasswordLayout.error = getString(R.string.password_dont_match_error)
                false
            }

            username.isEmpty() || username.isBlank() -> {
                binding.usernameLayout.error =
                    requireContext().getString(R.string.username_empty_error)
                false
            }*/

            else -> KeeperResult.Success(PASSWORD_IS_VALID)
        }
    }


    const val PASSWORD_MIN_LENGTH = 8

    const val PASSWORD_TOO_SHORT = "Password too short"
    const val PASSWORD_IS_BLANK = "Password is blank"
    const val PASSWORD_IS_VALID = "Password is valid"
}