package lv.maros.secured.password.keeper.pages.setup.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import lv.maros.secured.password.keeper.KeeperApplication


import lv.maros.secured.password.keeper.databinding.FragmentSelectAuthMethodBinding
import lv.maros.secured.password.keeper.pages.setup.SharedSetupViewModel
import lv.maros.secured.password.keeper.pages.setup.SharedSetupViewModelFactory
import lv.maros.secured.password.keeper.utils.KEEPER_AUTH_TYPE_PASSKEY
import lv.maros.secured.password.keeper.utils.KEEPER_AUTH_TYPE_PIN
import java.util.concurrent.Executor

class ConfigureAuthMethodFragment : Fragment() {

    private val viewModel: SharedSetupViewModel by activityViewModels()

    private lateinit var binding: FragmentSelectAuthMethodBinding

    // TEST
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSelectAuthMethodBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        binding.pinRadioButton.setOnClickListener {
            showPasskeyInputDialog(KEEPER_AUTH_TYPE_PIN)
        }

        binding.passwordRadioButton.setOnClickListener {
            showPasskeyInputDialog(KEEPER_AUTH_TYPE_PASSKEY)
        }

        binding.disableAuthRadioButton.setOnClickListener {
            test_biometric(it)
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

    private fun test_biometric(view: View) {
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(requireContext(),
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(requireContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(requireContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.

        biometricPrompt.authenticate(promptInfo)
    }

    private fun showPasskeyInputDialog(keeperAuthType: String) {
        PasskeyInputBottomDialog.newInstance(keeperAuthType)
            .show(requireActivity().supportFragmentManager, keeperAuthType)
    }

    private fun showToast(msg: String) { //TODO use error fields on TextView for errors
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}