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
import lv.maros.keeper.utils.PASSWORD_MIN_LENGTH
import lv.maros.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.keeper.utils.setTitle
import timber.log.Timber

@AndroidEntryPoint
class AddPasswordFragment : Fragment() {

    private lateinit var binding: FragmentAddPasswordBinding

    private val viewModel: SharedKeeperViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddPasswordBinding.inflate(inflater).also {
            it.lifecycleOwner = this.viewLifecycleOwner
            it.viewModel = viewModel
        }

        setTitle(getString(R.string.add_new_password))
        setDisplayHomeAsUpEnabled(true)

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        binding.save.setOnClickListener {
            verifyAndSavePassword()
        }

        binding.cancel.setOnClickListener {
            //TODO
        }
    }

    private fun verifyAndSavePassword() {
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

    //TODO I would move it into viewModel, but how to access the TextLayout error field then ? Don't have a good solution now.
    private fun verifyPasswordInputData(passwordData: PasswordInputData): Boolean {
        val (website, username, password, repeatPassword) = passwordData

        Timber.d("Verifying Password: $passwordData")

        resetAllTextInputLayouts(binding.addPasswordLayout)

        //TODO refactor it
        return when {
            password.isEmpty() || password.isBlank() -> {
                binding.passwordLayout.error =
                    requireContext().getString(R.string.password_empty_error)
                false
            }
            password.length < PASSWORD_MIN_LENGTH -> {
                binding.passwordLayout.error =
                    requireContext().getString(R.string.password_min_len_error)
                false
            }

            repeatPassword.isEmpty() || repeatPassword.isBlank() -> {
                binding.repeatPasswordLayout.error =
                    requireContext().getString(R.string.password_empty_error)
                false
            }
            repeatPassword.length < PASSWORD_MIN_LENGTH -> {
                binding.repeatPasswordLayout.error =
                    requireContext().getString(R.string.password_min_len_error)
                false
            }
            password != repeatPassword -> {
                binding.passwordLayout.error = getString(R.string.password_dont_match_error)
                binding.repeatPasswordLayout.error = getString(R.string.password_dont_match_error)
                false
            }

            username.isEmpty() || username.isBlank() -> {
                binding.usernameLayout.error =
                    requireContext().getString(R.string.username_empty_error)
                false
            }

            else -> true
        }
    }

    private fun resetAllTextInputLayouts(passwordLayout: ViewGroup) {
        val childCount = passwordLayout.childCount
        Timber.d("Child count = $childCount")
        for (i in 0..childCount) {
            val view = passwordLayout.getChildAt(i)

            if (view is TextInputLayout) {
                Timber.d("Found TextInputLayout at position $i")
                view.error = null
            }
        }
    }

    private fun showToast(msg: String) { //TODO use error fields on TextView for errors
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


}