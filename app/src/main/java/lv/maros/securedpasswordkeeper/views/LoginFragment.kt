package lv.maros.securedpasswordkeeper.views

import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels

import lv.maros.securedpasswordkeeper.R
import lv.maros.securedpasswordkeeper.SharedPasswordViewModel
import lv.maros.securedpasswordkeeper.authentication.Authenticator
import lv.maros.securedpasswordkeeper.databinding.FragmentLoginBinding
import lv.maros.securedpasswordkeeper.utils.KeeperMessageHandler


class LoginFragment : Fragment() {

    private lateinit var messageHandler: KeeperMessageHandler

    private lateinit var authenticator: Authenticator

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: SharedPasswordViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater).also {
            it.lifecycleOwner = this.viewLifecycleOwner
        }

        viewModel.authenticate()

       /* // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        val biometricLoginButton =
            findViewById<Button>(R.id.biometric_login)
        biometricLoginButton.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }*/



        return binding.root
    }
}