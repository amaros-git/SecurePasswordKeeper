package lv.maros.secured.password.keeper.pages.addEdit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.databinding.DialogPasswordGeneratorBinding
import lv.maros.secured.password.keeper.pages.generator.PasswordGeneratorViewModel
import lv.maros.secured.password.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.secured.password.keeper.utils.setTitle
import timber.log.Timber


class PasswordGeneratorDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogPasswordGeneratorBinding

    private fun configureViews() {
        binding.passwordGeneratorCancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.passwordGeneratorOkButton.setOnClickListener {
            val password = getGeneratedPassword()
            findNavController().navigate(
                PasswordGeneratorFragmentDirections
                    .actionPasswordGeneratorFragmentToPasswordAddEditFragment(password)
            )
        }
    }

    private fun getGeneratedPassword(): String {
        val text = null.toString()
        Timber.d("text = $text")
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

        configureViews()

        return binding.root
    }

    companion object {
        const val PASSWORD_GENERATOR_DIALOG_TAG = "PASSWORD_GENERATOR_TAG"
    }


}