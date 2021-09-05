package lv.maros.keeper.setup.views

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.keeper.R
import lv.maros.keeper.databinding.DialogAuthMethodBinding
import lv.maros.keeper.setup.SharedSetupViewModel
import lv.maros.keeper.utils.KEEPER_AUTH_TYPE_PASSKEY
import lv.maros.keeper.utils.KEEPER_AUTH_TYPE_PIN

@AndroidEntryPoint
class PasskeyInputBottomDialog(
    private val keeperAuthType: String
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
        when (keeperAuthType) {
            KEEPER_AUTH_TYPE_PIN -> {
                configurePinLayout()
            }
            KEEPER_AUTH_TYPE_PASSKEY -> {
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
        val passkey1 = binding.passkey.text.toString()
        val passkey2 = binding.repeatPasskey.text.toString()

       if (viewModel.verifyPasskeys(passkey1, passkey2)) {
           viewModel.completeAuthConfigurationAndNavigate(passkey1, keeperAuthType)
           dismiss()
        }
    }

    companion object {

        fun newInstance(keeperAuthType: String): PasskeyInputBottomDialog {
            return PasskeyInputBottomDialog(keeperAuthType)
        }
    }
}