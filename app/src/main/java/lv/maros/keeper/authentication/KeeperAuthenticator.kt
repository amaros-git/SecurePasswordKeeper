package lv.maros.keeper.authentication

import androidx.biometric.BiometricPrompt
import lv.maros.keeper.security.KeeperConfigStorage
import lv.maros.keeper.utils.KeeperResult
import java.util.concurrent.Executor
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class KeeperAuthenticator @Inject constructor(
    private val configStorage: KeeperConfigStorage
) {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    suspend fun requestAuthentication(): KeeperResult {
        return suspendCoroutine {

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