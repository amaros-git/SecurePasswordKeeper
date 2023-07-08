package lv.maros.secured.password.keeper.pages.setup.views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.databinding.FragmentSelectAuthMethodBinding
import lv.maros.secured.password.keeper.pages.setup.SharedSetupViewModel
import lv.maros.secured.password.keeper.utils.KEEPER_AUTH_TYPE_PASSKEY
import lv.maros.secured.password.keeper.utils.KEEPER_AUTH_TYPE_PIN
import timber.log.Timber
import java.util.concurrent.Executor

class ConfigureAuthMethodFragment : Fragment() {

    private val viewModel: SharedSetupViewModel by activityViewModels()

    private lateinit var binding: FragmentSelectAuthMethodBinding

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private fun setupViews() {
        binding.biometricRadioButton.setOnClickListener {
            checkBiometricAuthentication()
        }
        binding.pinRadioButton.setOnClickListener {
            showPasskeyInputDialog(KEEPER_AUTH_TYPE_PIN)
        }

        binding.passwordRadioButton.setOnClickListener {
            showPasskeyInputDialog(KEEPER_AUTH_TYPE_PASSKEY)
        }

        binding.disableAuthRadioButton.setOnClickListener {
            disableAuthentication()
        }

        viewModel.showToastEvent.observe(viewLifecycleOwner) {
            it?.let {
                showToast(it)
            }
        }

        viewModel.authenticationIsConfiguredEvent.observe(viewLifecycleOwner) {
            it?.let {
                findNavController()
                    .navigate(
                        ConfigureAuthMethodFragmentDirections
                            .actionSelectAuthMethodFragmentToFinishSetupFragment()
                    )
            }
        }
    }

    private fun disableAuthentication() {
        // for now just move to the next page
        moveToTheNextPage()
    }

    private fun checkBiometricAuthentication() {
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Timber.e("onAuthenticationError, errorCode = $errorCode, errString = $errString")
                    handleBiometricError(errorCode)
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    moveToTheNextPage()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Timber.e("Authentication failed")
                    //TODO wrong finger ? :),  but what next ?
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(requireContext().getString(R.string.app_name))
            .setSubtitle("Log in using your biometric credential")
            //yes, deprecated, but this new one API
            // .setAllowedAuthenticators( BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            // doesn't works as expected. I use it because don't want to set negative button.
            .setDeviceCredentialAllowed(true)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun handleBiometricError(errorCode: Int) {
        when(errorCode) {
            BiometricPrompt.ERROR_NO_BIOMETRICS -> {
                showToast("Please first enroll biometric in your phone")
            }
        }
    }

    private fun moveToTheNextPage() {
        findNavController().navigate(
            ConfigureAuthMethodFragmentDirections.actionSelectAuthMethodFragmentToFinishSetupFragment()
        )
    }

    private fun showPasskeyInputDialog(keeperAuthType: String) {
        PasskeyInputBottomDialog.newInstance(keeperAuthType)
            .show(requireActivity().supportFragmentManager, keeperAuthType)
    }

    private fun showToast(msg: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), msg, length).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("Hello")
        binding = FragmentSelectAuthMethodBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        setupViews()

        return binding.root
    }
}