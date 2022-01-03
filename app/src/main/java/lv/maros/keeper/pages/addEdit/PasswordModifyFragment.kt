package lv.maros.keeper.pages.addEdit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.keeper.R
import lv.maros.keeper.base.BaseFragment
import lv.maros.keeper.base.BaseViewModel
import lv.maros.keeper.databinding.FragmentModifyPasswordBinding
import lv.maros.keeper.models.Password
import lv.maros.keeper.models.PasswordInputData
import lv.maros.keeper.security.KeeperPasswordManager
import lv.maros.keeper.utils.KeeperResult
import lv.maros.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.keeper.utils.setTitle
import timber.log.Timber
import kotlin.properties.Delegates

@AndroidEntryPoint
class PasswordModifyFragment : BaseFragment() {

    private lateinit var binding: FragmentModifyPasswordBinding

    override val viewModel: PasswordModifyViewModel by viewModels()

    private var currentMode by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentModifyPasswordBinding.inflate(inflater)
        binding.lifecycleOwner = this.viewLifecycleOwner

        setDisplayHomeAsUpEnabled(true)

        configureFragmentMode(getMode())

        setupCommonViews()

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
                viewModel.loadPassword(passwordId)
            } else {
                //TODO
            }
        }
    }

    override fun onResume() {
        super.onResume()

        loadPassword()

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
        viewModel.password.observe(viewLifecycleOwner) {
            it?.let {
                showPassword(it)
            }
        }
        binding.applyButton.apply {
            text = requireContext().getText(R.string.update_password_button_text)

            setOnClickListener {
                resetTextInputLayoutsErrors(binding.passwordModificationLayout)

                viewModel.updatePassword(collectPasswordInputData())
            }
        }
        binding.cancelButton.text = requireContext().getText(R.string.back_button_text)
    }

    private fun setupAddMode() {
        binding.applyButton.apply {
            text = requireContext().getText(R.string.add_password_button_text)

            setOnClickListener {
                resetTextInputLayoutsErrors(binding.passwordModificationLayout)

                viewModel.savePassword(collectPasswordInputData())
            }
        }
        binding.cancelButton.text = requireContext().getText(R.string.cancel_button_text)
    }

    private fun setupCommonViews() {
        binding.cancelButton.setOnClickListener {
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
        binding.website.text.toString(),
        binding.username.text.toString(),
        binding.password.text.toString(),
        binding.repeatPassword.text.toString()
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