package lv.maros.securedpasswordkeeper.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint

import lv.maros.securedpasswordkeeper.SharedPasswordViewModel
import lv.maros.securedpasswordkeeper.authentication.KeeperAuthenticator
import lv.maros.securedpasswordkeeper.databinding.FragmentLoginBinding
import lv.maros.securedpasswordkeeper.utils.KeeperMessageHandler
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var messageHandler: KeeperMessageHandler

    private lateinit var keeperAuthenticator: KeeperAuthenticator

    private lateinit var binding: FragmentLoginBinding

    @Inject
    lateinit var viewModel: SharedPasswordViewModel

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