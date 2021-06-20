package lv.maros.securedpasswordkeeper.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint

import lv.maros.securedpasswordkeeper.SharedPasswordViewModel
import lv.maros.securedpasswordkeeper.authentication.AuthResult
import lv.maros.securedpasswordkeeper.databinding.FragmentLoginBinding
import lv.maros.securedpasswordkeeper.models.KeeperUser
import lv.maros.securedpasswordkeeper.utils.KeeperMessageHandler
import timber.log.Timber
import java.util.concurrent.Executor
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var messageHandler: KeeperMessageHandler

    private lateinit var binding: FragmentLoginBinding

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    @Inject
    lateinit var viewModel: SharedPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater).also {
            it.lifecycleOwner = this.viewLifecycleOwner
        }

        startBiometricAuthentication()

        return binding.root
    }

    private fun startBiometricAuthentication() {
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    viewModel.processAuthenticationResult(
                        AuthResult.Error(
                            "$errString: $errorCode"
                        )
                    )
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    viewModel.processAuthenticationResult(AuthResult.Success(
                        createSessionUser()
                    ))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    viewModel.processAuthenticationResult(AuthResult.Fail(
                        "Authentication failed"
                    ))
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        biometricPrompt.authenticate(promptInfo)

    }

    private fun createSessionUser(): KeeperUser {
        return KeeperUser(System.currentTimeMillis())
    }
}