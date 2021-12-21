package lv.maros.keeper.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.keeper.R
import lv.maros.keeper.SharedKeeperViewModel
import lv.maros.keeper.databinding.FragmentAddPasswordBinding
import lv.maros.keeper.models.PasswordInputData
import lv.maros.keeper.security.KeeperPasswordManager
import lv.maros.keeper.utils.KeeperResult
import lv.maros.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.keeper.utils.setTitle
import timber.log.Timber
import java.sql.Time
import kotlin.properties.Delegates

@AndroidEntryPoint
class PasswordAddEditFragment : Fragment() {

    private lateinit var binding: FragmentAddPasswordBinding

    private val viewModel: SharedKeeperViewModel by activityViewModels()

    private var currentMode = MODE_ADD_PASSWORD //default mode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddPasswordBinding.inflate(inflater).also {
            it.lifecycleOwner = this.viewLifecycleOwner
            it.viewModel = viewModel
        }

        configureThisFragment()

        setupViews()

        return binding.root
    }

    private fun configureThisFragment() {
        setTitle(getString(R.string.add_new_password))
        setDisplayHomeAsUpEnabled(true)

        arguments?.getInt(getString(R.string.password_add_edit_fragment_mode))
            ?.let { receivedMode ->
                currentMode = receivedMode
            }

        Timber.d ("Starting fragment with mode $currentMode")
    }

    private fun setupViews() {
        binding.save.setOnClickListener {
            resetAllTextInputLayouts(binding.addPasswordLayout)

            collectVerifyAndSavePassword()
        }

        binding.cancel.setOnClickListener {
            //TODO
        }
    }

    private fun collectVerifyAndSavePassword() {
        val passwordData = collectPasswordInputData()

        if (verifyPasswordInputData(passwordData)) {
            viewModel.saveAndNavigateIfSuccess(passwordData)
        }
    }

    private fun collectPasswordInputData() = PasswordInputData(
        binding.website.text.toString(),
        binding.username.text.toString(),
        binding.password.text.toString(),
        binding.repeatPassword.text.toString()
    )

    private fun resetAllTextInputLayouts(passwordLayout: ViewGroup) {
        val childCount = passwordLayout.childCount
        for (i in 0..childCount) {
            val view = passwordLayout.getChildAt(i)

            if (view is TextInputLayout) {
                view.error = null
            }
        }
    }

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
    }

}