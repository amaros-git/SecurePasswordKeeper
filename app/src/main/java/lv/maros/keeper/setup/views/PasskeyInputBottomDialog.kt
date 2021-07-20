package lv.maros.keeper.setup.views

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Keep
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.keeper.R
import lv.maros.keeper.authentication.KeeperAuthenticator
import lv.maros.keeper.databinding.DialogAuthMethodBinding
import lv.maros.keeper.security.KeeperCryptor
import lv.maros.keeper.setup.CryptoResult
import javax.inject.Inject

@AndroidEntryPoint
class PasskeyInputBottomDialog(
    private val authType: Int
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogAuthMethodBinding

    @Inject
    lateinit var authenticator: KeeperAuthenticator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAuthMethodBinding.inflate(inflater)

        setupViews()

        return binding.root
    }

    //TODO - create data class and provide to layout. REFACTOR
    private fun setupViews() {
        when (authType) {
            AUTH_TYPE_PIN -> {
                configurePinLayout()
            }
            AUTH_TYPE_PASSWORD -> {
                configurePasswordLayout()
            }
        }

        binding.cancel.setOnClickListener {
            this.dismiss()
        }

        binding.save.setOnClickListener {
            processPasskeys()
        }
    }

    private fun configurePasswordLayout() {
        binding.apply {
            passkeyLayout.hint = resources.getText(R.string.password_hint_text)
            passkey.inputType = InputType.TYPE_CLASS_TEXT

            repeatPasskeyLayout.hint = resources.getText(R.string.password_repeat_hint_text)
            repeatPasskey.inputType = InputType.TYPE_CLASS_TEXT
        }
    }

    private fun configurePinLayout() {
        binding.apply {
            passkeyLayout.hint = resources.getText(R.string.pin_hint_text)
            passkey.inputType = InputType.TYPE_CLASS_NUMBER

            repeatPasskeyLayout.hint = resources.getText(R.string.pin_repeat_hint_text)
            repeatPasskey.inputType = InputType.TYPE_CLASS_NUMBER
        }
    }

    private fun processPasskeys() {
        val hash = collectAndHashIfValid()

        if (null != hash) {
            val bundle = Bundle().apply {
                putString(SelectAuthMethodFragment.PASSKEY_HASH_KEY, hash)
            }
            setFragmentResult(SelectAuthMethodFragment.DIALOG_RESULT_KEY, bundle)

            this.dismiss() // Dismiss dialog only when success
        }
        else {
            showToast("Internal error, please try again")
        }
    }

    private fun verifyPasskeys(passkey1: String, passkey2: String): Boolean {
        return if (passkey1.isEmpty() || passkey2.isEmpty()) {
            showToast(requireContext().resources.getString(R.string.passkey_empty_error))
            false
        }
        else if ((passkey1.length < KeeperAuthenticator.PASSKEY_MIN_LENGTH) ||
                (passkey2.length < KeeperAuthenticator.PASSKEY_MIN_LENGTH)) {
            showToast(requireContext().resources.getString(R.string.passkey_min_len_error))
            false
        }
        else if (passkey1 != passkey2) {
            showToast(requireContext().resources.getString(R.string.passkey_dont_match_error))
            false
        }
        else passkey1 == passkey2
    }

    private fun collectAndHashIfValid(): String? {
        val passkey1 = binding.passkey.text.toString()
        val passkey2 = binding.repeatPasskey.text.toString()

        if (verifyPasskeys(passkey1, passkey2)) {
            KeeperCryptor.hashData(passkey1)
        }
    }


    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


    companion object {

        const val AUTH_TYPE_PIN = 1
        const val AUTH_TYPE_PASSWORD = 2

        const val PASSKEY_MIN_LENGTH = 4

        fun newInstance(authType: Int): PasskeyInputBottomDialog {
            return PasskeyInputBottomDialog(authType)
        }
    }
}