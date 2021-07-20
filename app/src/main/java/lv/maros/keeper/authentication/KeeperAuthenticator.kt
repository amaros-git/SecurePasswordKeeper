package lv.maros.keeper.authentication

import androidx.biometric.BiometricPrompt
import kotlinx.coroutines.*
import lv.maros.keeper.security.KeeperConfigStorage
import lv.maros.keeper.security.KeeperCryptor
import lv.maros.keeper.utils.KeeperResult
import java.util.concurrent.Executor
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class KeeperAuthenticator(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    @Inject lateinit var configStorage: KeeperConfigStorage

    suspend fun requestAuthentication(): KeeperResult {
        return suspendCoroutine {

        }
    }

    fun isPasskeyLegal(passkey: String): Boolean {
        return (passkey.isNotEmpty()) &&
                (passkey.isNotBlank()) &&
                (passkey.length >= KeeperCryptor.)
        // TODO spaces ?
    }

    fun verifyPasskey(passkey: String): Boolean {
        val hashCurrent = KeeperCryptor.hashData(passkey)
        val hashSaved =
            configStorage.getKeeperConfigParam(KeeperConfigStorage.KEEPER_CONFIG_PASSKEY_HASH)
        return when {
            hashCurrent.isNullOrEmpty() || hashSaved.isNullOrEmpty() -> {
                false
            }
            hashCurrent != hashSaved -> {
                false
            }
            hashCurrent == hashSaved -> {
                true
            }
            // shall not happen
            else -> false
        }
    }



        /*executor = ContextCompat.getMainExecutor(requireContext())
    biometricPrompt = BiometricPrompt(this, executor,
    object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(
            errorCode: Int,
            errString: CharSequence
        ) {
            super.onAuthenticationError(errorCode, errString)
            Toast.makeText(
                requireContext(),
                "Authentication error: $errString", Toast.LENGTH_SHORT
            )
                .show()
        }

        override fun onAuthenticationSucceeded(
            result: BiometricPrompt.AuthenticationResult
        ) {
            super.onAuthenticationSucceeded(result)
            Toast.makeText(
                requireContext(),
                "Authentication succeeded!", Toast.LENGTH_SHORT
            )
                .show()
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            Toast.makeText(
                requireContext(), "Authentication failed",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    })

    promptInfo = BiometricPrompt.PromptInfo.Builder()
    .setTitle("Biometric login for my app")
    .setSubtitle("Log in using your biometric credential")
    .setNegativeButtonText("Use account password")
    .build()

    biometricPrompt.authenticate(promptInfo)*/

    companion object {
        const val PASSKEY_MIN_LENGTH = 4
    }
}