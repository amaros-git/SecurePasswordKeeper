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
import lv.maros.keeper.databinding.FragmentAddEditPasswordBinding
import lv.maros.keeper.models.PasswordInputData
import lv.maros.keeper.security.KeeperPasswordManager
import lv.maros.keeper.utils.KeeperResult
import lv.maros.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.keeper.utils.setTitle
import timber.log.Timber
import kotlin.properties.Delegates

@AndroidEntryPoint
class PasswordAddEditFragment : Fragment() {

    private lateinit var binding: FragmentAddEditPasswordBinding

    private val viewModel: PasswordAddEditViewModel by viewModels()

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

        return binding.root
    }

    private fun getMode(): Int {
        currentMode = arguments?.getInt("mode") ?: MODE_UNSUPPORTED
        return currentMode
    }

    private fun getPasswordId(): Int {
        return arguments?.getInt("passwordId") ?: -1
    }

    private fun loadPassword() {
        if (currentMode == MODE_EDIT_PASSWORD) {
            val passwordId = getPasswordId()
            if (passwordId > 0) {
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

    /*//TODO blocking call, show Progress
    private fun getPassword(passwordId: Int): Password? {
        return viewModel.getPassword(passwordId)
    }*/

    private fun configureFragmentMode(mode: Int) {
        when (getMode()) {
            MODE_EDIT_PASSWORD -> {
                setTitle(getString(R.string.edit_password))
                setupEditMode()
            }
            MODE_ADD_PASSWORD -> {
                setTitle(getString(R.string.add_new_password))
                setupAddMode()
            }
            else -> {
                //TODO
            }
        }
    }

    private fun setupEditMode() {
        binding.applyButton.apply {
            text = requireContext().getText(R.string.edit_password)

            setOnClickListener {
                resetTextInputLayoutsErrors(binding.addPasswordLayout)
            }
        }

        viewModel.password.observe(viewLifecycleOwner) {
            it?.let {
                Timber.d("Got password $it")
            }
        }
    }

    private fun setupAddMode() {
        binding.applyButton.apply {
            text = requireContext().getText(R.string.add_new_password)

            setOnClickListener {
                resetTextInputLayoutsErrors(binding.addPasswordLayout)
                collectVerifyAndSavePassword()
            }
        }
    }

    private fun setupCommonViews() {
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun collectVerifyAndSavePassword() {
        val passwordData = collectPasswordInputData()

        if (verifyPasswordInputData(passwordData)) {
            viewModel.savePassword(passwordData)
        }
    }

    private fun collectPasswordInputData() = PasswordInputData(
        binding.website.text.toString(),
        binding.username.text.toString(),
        binding.password.text.toString(),
        binding.repeatPassword.text.toString()
    )

    private fun resetTextInputLayoutsErrors(passwordLayout: ViewGroup) {
        val childCount = passwordLayout.childCount
        for (i in 0..childCount) {
            val view = passwordLayout.getChildAt(i)
            if (view is TextInputLayout) {
                view.error = null
            }
        }
    }

    //TODO move to ViewModel. Rework, compare passwords etc
    private fun verifyPasswordInputData(passwordData: PasswordInputData): Boolean {
        val (website, username, password, repeatPassword) = passwordData
        // 1. Check both password
        val passwordResult = KeeperPasswordManager.verifyPassword(password)
        if (passwordResult is KeeperResult.Error) {
            binding.passwordLayout.error =
                convertToUiErrorString(passwordResult.value)
            return false
        }

        val repeatPasswordResult = KeeperPasswordManager.verifyPassword(repeatPassword)
        if (repeatPasswordResult is KeeperResult.Error) {
            binding.repeatPasswordLayout.error =
                convertToUiErrorString(repeatPasswordResult.value)
            return false
        }

        //2. Check username

        return true
    }

    private fun convertToUiErrorString(passwordError: String): String =
        when (passwordError) {
            KeeperPasswordManager.PASSWORD_TOO_SHORT -> {
                getString(R.string.password_min_len_error)
            }
            KeeperPasswordManager.PASSWORD_IS_BLANK -> {
                getString(R.string.password_empty_error)
            }

            else -> getString(R.string.internal_error)
        }


    private fun showToast(msg: String) { //TODO use error fields on TextView for errors
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val MODE_ADD_PASSWORD = 0
        const val MODE_EDIT_PASSWORD = 1
        const val MODE_UNSUPPORTED = -1
    }

}