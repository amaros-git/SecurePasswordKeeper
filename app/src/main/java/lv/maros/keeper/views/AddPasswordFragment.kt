package lv.maros.keeper.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.keeper.models.Password
import lv.maros.keeper.R
import lv.maros.keeper.SharedKeeperViewModel
import lv.maros.keeper.databinding.FragmentAddPasswordBinding
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

        setTitle(getString(R.string.app_name))
        setDisplayHomeAsUpEnabled(true)

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        binding.save.setOnClickListener {
            verifyAndSavePassword()
        }
    }

    private fun verifyAndSavePassword() {
        val tempPassword = getPasswordFromViews()
        val (id, description, url, username, plainPassword, date) = getPasswordFromViews()

        if (verifyPassword(tempPassword)) {
            val encryptedPassword = viewModel.encryptString(plainPassword)
            if (null != encryptedPassword) {
                val finalPassword =
                    Password(id, description, url, username, encryptedPassword, date)
                Timber.d("Final password = $finalPassword")
                viewModel.savePassword(finalPassword)
            } else {
                showToast("System error: cannot encrypt. Please restart app and/or phone")
            }
        }
    }

    private fun verifyPassword(password: Password): Boolean {
        val (_, _, _, username, plainPassword) = getPasswordFromViews()

        return if (plainPassword.isEmpty() && username.isEmpty()) {
            showToast("Username or password is empty")
            false
        } else if (plainPassword.length < PASSWORD_MIN_LENGTH) {
            showToast("Password is too short, min length is $PASSWORD_MIN_LENGTH")
            false
        } else true
    }


    private fun getPasswordFromViews() = Password(
        0,
        binding.description.text.toString(),
        binding.url.text.toString(),
        binding.username.text.toString(),
        binding.password.text.toString(),
        System.currentTimeMillis()
    )

    private fun showToast(msg: String) { //TODO use error fields on TextView for errors
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


}