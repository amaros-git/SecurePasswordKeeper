package lv.maros.secured.password.keeper.pages.passwords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.databinding.DialogPasswordGeneratorBinding
import lv.maros.secured.password.keeper.security.KeeperPasswordManager
import lv.maros.secured.password.keeper.security.PasswordGeneratorConfig
import lv.maros.secured.password.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.secured.password.keeper.utils.setTitle
import timber.log.Timber


class PasswordSearchDialog private constructor(
    private val viewModel: PasswordAddEditViewModel
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogPasswordGeneratorBinding

    private val passwordLengthRange = (8..24) //TODO move out somewhere to have access from others

    private fun configureViews() {
        setClickListeners()
        configureSpinner()
    }

    private fun setClickListeners() {
        binding.passwordGeneratorOkButton.setOnClickListener {
            viewModel.saveGeneratedPassword(getGeneratedPassword())
            this.dismiss()
        }

        binding.passwordGeneratorCancelButton.setOnClickListener {
            this.dismiss()
        }

        binding.passwordGeneratorGenerateButton.setOnClickListener {
            generatePassword()
        }
    }

    private fun configureSpinner() {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            passwordLengthRange.toList().toTypedArray()
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.passwordGeneratorPasswordLengthSpinner.adapter = it
        }
    }

    private fun generatePassword() {
        val password = KeeperPasswordManager.generatePassword(
            getPasswordLength(),
            getPasswordGeneratorConfig()
        )
        binding.passwordGeneratorPasswordText.setText(password)
    }

    private fun getPasswordGeneratorConfig() = PasswordGeneratorConfig(
        binding.passwordGeneratorLettersCheck.isChecked,
        binding.passwordGeneratorDigitsCheck.isChecked,
        binding.passwordGeneratorSymbolsCheck.isChecked

    )

    private fun getPasswordLength() =
        binding.passwordGeneratorPasswordLengthSpinner.selectedItem.toString().toInt()

    private fun getGeneratedPassword(): String {
        return binding.passwordGeneratorPasswordText.text.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPasswordGeneratorBinding.inflate(inflater)
        binding.lifecycleOwner = this.viewLifecycleOwner

        setTitle(getString(R.string.password_generator_fragment_title))
        setDisplayHomeAsUpEnabled(true)
        dialog?.setCanceledOnTouchOutside(false)

        configureViews()

        return binding.root
    }

    companion object {
        const val PASSWORD_GENERATOR_DIALOG_TAG = "PASSWORD_GENERATOR_TAG"

        fun newInstance(viewModel: PasswordAddEditViewModel): PasswordSearchDialog {
            return PasswordSearchDialog(viewModel)
        }
    }
}