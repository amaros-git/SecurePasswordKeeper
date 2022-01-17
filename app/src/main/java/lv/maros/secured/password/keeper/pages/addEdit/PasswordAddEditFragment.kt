package lv.maros.secured.password.keeper.pages.addEdit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.base.BaseFragment
import lv.maros.secured.password.keeper.databinding.FragmentAddEditPasswordBinding
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.models.PasswordInputData
import lv.maros.secured.password.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.secured.password.keeper.utils.setTitle
import kotlin.properties.Delegates

@AndroidEntryPoint
class PasswordAddEditFragment : BaseFragment() {

    private lateinit var binding: FragmentAddEditPasswordBinding

    override val _viewModel: PasswordAddEditViewModel by viewModels()

    private var currentMode by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddEditPasswordBinding.inflate(inflater)
        binding.lifecycleOwner = this.viewLifecycleOwner

        setDisplayHomeAsUpEnabled(true)

        configureFragmentMode(getMode())

        setupCommonViews()

        observeTextInoutErrors()

        return binding.root
    }

    private fun getMode(): Int {
        currentMode = arguments?.getInt("mode") ?: MODE_UNSUPPORTED
        return currentMode
    }

    private fun loadPassword() {
        if (MODE_EDIT_PASSWORD == currentMode) {
            val passwordId = arguments?.getInt("passwordId") ?: WRONG_PASSWORD_ID
            if (passwordId >= 0) {
                _viewModel.loadPassword(passwordId)
            } else {
                //TODO
            }
        }
    }

    override fun onResume() {
        super.onResume()

        loadPassword()

    }

    private fun observeTextInoutErrors() {
        _viewModel.passwordError.observe(viewLifecycleOwner) {
            binding.passwordLayout.error = it
        }

        _viewModel.repeatPasswordError.observe(viewLifecycleOwner) {
            binding.repeatPasswordLayout.error = it
        }
    }

    private fun configureFragmentMode(mode: Int) {
        when (getMode()) {
            MODE_EDIT_PASSWORD -> {
                setTitle(getString(R.string.edit_password_title))
                setupEditMode()
            }
            MODE_ADD_PASSWORD -> {
                setTitle(getString(R.string.add_new_password_title))
                setupAddMode()
            }
            else -> {
                //TODO
            }
        }
    }

    private fun setupEditMode() {
        _viewModel.password.observe(viewLifecycleOwner) {
            it?.let {
                showPassword(it)
            }
        }

        binding.addEditApplyButton.apply {
            text = requireContext().getText(R.string.update_password_button_text)

            setOnClickListener {
                resetTextInputLayoutsErrors(binding.passwordModificationLayout)

                _viewModel.updatePassword(collectPasswordInputData())
            }
        }

        binding.addEditCancelButton.text = requireContext().getText(R.string.back_button_text)
    }

    private fun setupAddMode() {
        binding.addEditApplyButton.apply {
            text = requireContext().getText(R.string.add_password_button_text)

            setOnClickListener {
                resetTextInputLayoutsErrors(binding.passwordModificationLayout)

                _viewModel.savePassword(collectPasswordInputData())
            }
        }
        binding.addEditCancelButton.text = requireContext().getText(R.string.cancel_button_text)
    }

    private fun setupCommonViews() {
        binding.addEditCancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showPassword(password: Password) {
        val (website, username, encryptedPassword) = password

        binding.websiteLayout.editText?.setText(website)
        binding.usernameLayout.editText?.setText(username)
        binding.passwordLayout.editText?.setText(encryptedPassword)
    }


    private fun collectPasswordInputData() = PasswordInputData(
        binding.websiteEditText.text.toString(),
        binding.usernameEditText.text.toString(),
        binding.passwordEditText.text.toString(),
        binding.repeatPasswordEditText.text.toString()
    )

    private fun resetTextInputLayoutsErrors(layout: ViewGroup) {
        val childCount = layout.childCount
        for (i in 0..childCount) {
            val view = layout.getChildAt(i)
            if (view is TextInputLayout) {
                view.error = null
            }
        }
    }

    private fun showToast(msg: String) { //TODO use error fields on TextView for errors
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val MODE_ADD_PASSWORD = 0
        const val MODE_EDIT_PASSWORD = 1
        const val MODE_UNSUPPORTED = -1

        const val WRONG_PASSWORD_ID = -1
    }

}