package lv.maros.secured.password.keeper.pages.addEdit

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


class PasswordGeneratorDialog private constructor(
    private val viewModel: PasswordAddEditViewModel
): BottomSheetDialogFragment() {

    private lateinit var binding: DialogPasswordGeneratorBinding

    private fun configureViews() {
        viewModel.generatedPassword.observe(viewLifecycleOwner) {
            binding.passwordGeneratorPasswordText.setText(it)
        }

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
        val intArray = intArrayOf(0, 1, 3).toTypedArray()

        ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, intArray).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.passwordGeneratorPasswordLengthSpinner.adapter = it
        }
    }


    private fun generatePassword() {
        val password = KeeperPasswordManager.generatePassword(8)
        Timber.d("password = $password")
        viewModel.saveGeneratedPassword(password)
    }

    private fun getGeneratedPassword(): String {
        return binding.passwordGeneratorPasswordText.text.toString()

    }

    private fun getPasswordGeneratorConfig() = PasswordGeneratorConfig(
        binding.passwordGeneratorSymbolsCheck.isChecked,
        binding.passwordGeneratorDigitsCheck.isChecked,
        binding.passwordGeneratorSymbolsCheck.isChecked

    )

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

        fun newInstance(viewModel: PasswordAddEditViewModel): PasswordGeneratorDialog {
            return PasswordGeneratorDialog(viewModel)
        }
    }


}