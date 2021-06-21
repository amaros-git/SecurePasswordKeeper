package lv.maros.securedpasswordkeeper.setup.views

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import lv.maros.securedpasswordkeeper.R
import lv.maros.securedpasswordkeeper.databinding.DialogAuthMethodBinding
import lv.maros.securedpasswordkeeper.databinding.FragmentSelectAuthMethodBinding
import timber.log.Timber

class AuthMethodBottomDialog(private val authType: Int) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogAuthMethodBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Timber.d("onCreateView called")
        binding = DialogAuthMethodBinding.inflate(inflater)

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        when (authType) {
            AUTH_TYPE_PIN -> {
                binding.passKeyLayout.hint = resources.getText(R.string.pin_hint_text)
                binding.passKey.inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD
            }
            AUTH_TYPE_PASSWORD -> {
                binding.passKeyLayout.hint = resources.getText(R.string.pin_hint_text)
                binding.passKey.inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD

            }
        }
    }

    companion object {

        const val AUTH_TYPE_BIOMETRIC = 1 //TODO do I need it here ?
        const val AUTH_TYPE_PIN = 2
        const val AUTH_TYPE_PASSWORD = 3

        fun newInstance(authType: Int): AuthMethodBottomDialog {
            return AuthMethodBottomDialog(authType)
        }
    }
}