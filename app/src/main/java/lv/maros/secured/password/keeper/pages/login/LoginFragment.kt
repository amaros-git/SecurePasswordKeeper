package lv.maros.secured.password.keeper.pages.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import lv.maros.secured.password.keeper.KeeperApplication
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.databinding.FragmentLoginBinding
import lv.maros.secured.password.keeper.models.KeeperUser
import lv.maros.secured.password.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.secured.password.keeper.utils.setTitle

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            (requireContext().applicationContext as KeeperApplication).configStorage,
            (requireContext().applicationContext as KeeperApplication)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // if login is not enabled, navigate to Password List
        if(!viewModel.isLoginEnabled()) {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToPasswordsFragment()
            )
        }
    }

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

        /*viewModel.authenticationResult.observe(viewLifecycleOwner) {
            handleAuthResult(it)
        }*/


        return binding.root
    }

    // Login is the first Fragment in nav graph, so if login is not enabled (default)
    // simply navigate to Password List Fragment :)


    private fun configureViews() {
        binding.login.setOnClickListener {
            val passkey = binding.passkey.text.toString()
            viewModel.verifyPasskey(passkey)
        }

    }
/*
    private fun handleAuthResult(keeperResult: KeeperResult) {
        if (keeperResult is KeeperResult.Success) {
            findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToPasswordListFragment())
        } else {
            val result = keeperResult as KeeperResult.Error
            Timber.d("authentication failed: ${result.value}")
        }
    }*/



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