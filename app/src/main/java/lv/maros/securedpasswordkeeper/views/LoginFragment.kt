package lv.maros.securedpasswordkeeper.views

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.from
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

import lv.maros.securedpasswordkeeper.R
import lv.maros.securedpasswordkeeper.utils.KeeperMessageHandler
import java.util.concurrent.Executor


class LoginFragment : Fragment() {

    private lateinit var messageHandler: KeeperMessageHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val looper = (Looper.myLooper()) ?: Looper.getMainLooper()
        messageHandler = KeeperMessageHandler.getInstance(looper)

       /* // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        val biometricLoginButton =
            findViewById<Button>(R.id.biometric_login)
        biometricLoginButton.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }*/

        return inflater.inflate(R.layout.fragment_login, container, false)
    }
}