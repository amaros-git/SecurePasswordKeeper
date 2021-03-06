package lv.maros.secured.password.keeper.pages.setup.views

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import lv.maros.secured.password.keeper.KeeperApplication
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.databinding.DialogAuthMethodBinding
import lv.maros.secured.password.keeper.pages.setup.SharedSetupViewModel
import lv.maros.secured.password.keeper.pages.setup.SharedSetupViewModelFactory
import lv.maros.secured.password.keeper.utils.KEEPER_AUTH_TYPE_PASSKEY
import lv.maros.secured.password.keeper.utils.KEEPER_AUTH_TYPE_PIN

class PasskeyInputBottomDialog private constructor(
    private val keeperAuthType: String
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogAuthMethodBinding

    private val viewModel: SharedSetupViewModel by activityViewModels()

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

        binding.authMethodCancelButton.setOnClickListener {
            this.dismiss()
        }

        binding.authMethodSaveButton.setOnClickListener {
            processPasskeys()
        }
    }

    private fun configurePasswordLayout() {
        binding.apply {
            passkeyLayout.hint = resources.getText(R.string.password_hint)
            passkey.inputType = InputType.TYPE_CLASS_TEXT

            repeatPasskeyLayout.hint = resources.getText(R.string.password_repeat_hint)
            repeatPasskey.inputType = InputType.TYPE_CLASS_TEXT
        }
    }

    private fun configurePinLayout() {
        binding.apply {
            passkeyLayout.hint = resources.getText(R.string.pin_hint)
            passkey.inputType = InputType.TYPE_CLASS_NUMBER

            repeatPasskeyLayout.hint = resources.getText(R.string.pin_repeat_hint)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAuthMethodBinding.inflate(inflater)

        dialog?.setCanceledOnTouchOutside(false)

        setupViews()

        return binding.root
    }

    companion object {

        fun newInstance(keeperAuthType: String): PasskeyInputBottomDialog {
            return PasskeyInputBottomDialog(keeperAuthType)
        }
    }
}