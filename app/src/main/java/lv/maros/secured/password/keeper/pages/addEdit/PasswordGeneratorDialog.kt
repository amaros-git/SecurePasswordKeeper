package lv.maros.secured.password.keeper.pages.addEdit

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.databinding.DialogPasswordGeneratorBinding
import lv.maros.secured.password.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.secured.password.keeper.utils.setTitle
import timber.log.Timber


class PasswordGeneratorDialog private constructor(
    private val viewModel: PasswordAddEditViewModel
): BottomSheetDialogFragment() {

    private lateinit var binding: DialogPasswordGeneratorBinding

    init {

    }

    private fun configureViews() {
        binding.passwordGeneratorCancelButton.setOnClickListener {
            this.dismiss()
        }

        binding.passwordGeneratorOkButton.setOnClickListener {
            viewModel.saveGeneratedPassword(getGeneratedPassword())
            this.dismiss()
        }
    }

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

        fun newInstance(viewModel: PasswordAddEditViewModel): PasswordGeneratorDialog {
            return PasswordGeneratorDialog(viewModel)
        }
    }


}