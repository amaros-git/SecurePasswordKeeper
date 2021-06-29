package lv.maros.securedpasswordkeeper.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.securedpasswordkeeper.R

import lv.maros.securedpasswordkeeper.SharedKeeperViewModel
import lv.maros.securedpasswordkeeper.authentication.AuthResult
import lv.maros.securedpasswordkeeper.databinding.FragmentLoginBinding
import lv.maros.securedpasswordkeeper.models.KeeperUser
import lv.maros.securedpasswordkeeper.setup.SharedSetupViewModel
import lv.maros.securedpasswordkeeper.utils.KeeperMessageHandler
import lv.maros.securedpasswordkeeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.securedpasswordkeeper.utils.setTitle
import timber.log.Timber
import java.util.concurrent.Executor
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: SharedKeeperViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater).also {
            it.lifecycleOwner = viewLifecycleOwner
        }

        setTitle(getString(R.string.login_fragment))
        setDisplayHomeAsUpEnabled(false)

        configureViews()

        viewModel.authenticationResult.observe(viewLifecycleOwner) {
            handleAuthResult(it)
        }


        return binding.root
    }

    private fun configureViews() {

        binding.login.setOnClickListener {
            val passkey = binding.passkey.text.toString()
            viewModel.verifyPasskey(passkey)
        }

    }

    private fun handleAuthResult(authResult: AuthResult) {
        if (authResult is AuthResult.Success) {
            findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToPasswordListFragment())
        } else {
            val result = authResult as AuthResult.Error
            Timber.d("authentication failed: ${result.msgType}")
        }
    }



    /*private fun startBiometricAuthentication() {
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

    }*/

    private fun createSessionUser(): KeeperUser {
        return KeeperUser(System.currentTimeMillis())
    }
}