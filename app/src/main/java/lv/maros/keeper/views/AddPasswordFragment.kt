package lv.maros.keeper.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.keeper.models.Password
import lv.maros.keeper.R
import lv.maros.keeper.SharedKeeperViewModel
import lv.maros.keeper.databinding.FragmentAddPasswordBinding
import lv.maros.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.keeper.utils.setTitle

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
        val password = getPasswordFromViews()
        if (isInputDataValid(password)) {
            //viewModel.savePassword(password)
        }
    }

    private fun isInputDataValid(password: Password): Boolean {
        return (password.description.isNotEmpty() &&
                password.username.isNotEmpty() &&
                password.encryptedPassword.isNotEmpty() &&
                password.username.length >= USERNAME_MIN_LENGTH &&
                password.encryptedPassword.length >= USERNAME_MIN_LENGTH)
    }


    private fun getPasswordFromViews() = Password(
        0,
        binding.description.text.toString(),
        binding.url.text.toString(),
        binding.username.text.toString(),
        binding.password.text.toString(),
        System.currentTimeMillis()
    )

    companion object {
        const val USERNAME_MIN_LENGTH = 4
    }

}