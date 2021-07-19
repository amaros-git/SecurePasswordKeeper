package lv.maros.keeper.setup.views

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Keep
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import lv.maros.keeper.R
import lv.maros.keeper.databinding.DialogAuthMethodBinding
import lv.maros.keeper.setup.SharedSetupViewModel
import lv.maros.keeper.utils.KeeperResult

class PasskeyInputBottomDialog(
    private val authType: Int
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogAuthMethodBinding

    private val viewModel: SharedSetupViewModel by activityViewModels()

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
        val passkey = collectAndVerify()
        passkey?.let {
            viewModel.encryptAndSavePasskey(it)

            this.dismiss() // Dismiss dialog only when success
        }
    }

    private fun collectAndVerify(): String? {
        val passkey1 = binding.passkey.text.toString()
        val passkey2 = binding.repeatPasskey.text.toString()

        val result = viewModel.verifyPasskeys(passkey1, passkey2)
        return if (result is KeeperResult.Success) {
            result.value
        }else {
            showToast((result as KeeperResult.Error).value)
            null
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