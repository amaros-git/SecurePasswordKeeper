package lv.maros.securedpasswordkeeper.setup.views

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import lv.maros.securedpasswordkeeper.R
import lv.maros.securedpasswordkeeper.SharedKeeperViewModel
import lv.maros.securedpasswordkeeper.databinding.DialogAuthMethodBinding
import lv.maros.securedpasswordkeeper.setup.SharedSetupViewModel
import timber.log.Timber

class PasskeyInputBottomDialog(
    private val viewModel: SharedSetupViewModel,
    private val authType: Int) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogAuthMethodBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAuthMethodBinding.inflate(inflater)

        setupViews()

        return binding.root
    }

    //TODO - create data class and provide to layout
    private fun setupViews() {
        when (authType) {
            AUTH_TYPE_PIN -> {
                val inputType = InputType.TYPE_CLASS_NUMBER
                binding.apply {
                    passkeyLayout.hint = resources.getText(R.string.pin_hint_text)
                    passkey.inputType = inputType

                    repeatPasskeyLayout.hint = resources.getText(R.string.pin_repeat_hint_text)
                    repeatPasskey.inputType = inputType
                }

            }
            AUTH_TYPE_PASSWORD -> {
                val inputType = InputType.TYPE_CLASS_TEXT
                binding.apply {
                    passkeyLayout.hint = resources.getText(R.string.password_hint_text)
                    passkey.inputType = inputType

                    repeatPasskeyLayout.hint = resources.getText(R.string.password_repeat_hint_text)
                    repeatPasskey.inputType = inputType
                }
            }
        }

        binding.cancel.setOnClickListener {
            this.dismiss()
        }

        binding.save.setOnClickListener {
            //val passKey = collectPasskey()
            viewModel.savePasskey("HUJ")
            this.dismiss()
        }
    }

    companion object {

        const val AUTH_TYPE_PIN = 1
        const val AUTH_TYPE_PASSWORD = 2

        fun newInstance(viewModel: SharedSetupViewModel, authType: Int): PasskeyInputBottomDialog {
            return PasskeyInputBottomDialog(viewModel, authType)
        }
    }
}