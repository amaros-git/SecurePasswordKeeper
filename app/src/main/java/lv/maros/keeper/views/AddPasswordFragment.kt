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
            viewModel.savePassword(passwordData)
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
        Timber.d("Child count = $childCount")
        for (i in 0..childCount) {
            val view = passwordLayout.getChildAt(i)

            if (view is TextInputLayout) {
                Timber.d("Found TextInputLayout at position $i")
                view.error = null
            }
        }
    }

    //TODO
    private fun verifyPasswordInputData(passwordData: PasswordInputData): Boolean {
        val(website, username, password, repeatPassword) = passwordData
        // 1. Check password
        val passwordResult = KeeperPasswordManager.verifyPassword(password)


        return true
    }


    private fun showToast(msg: String) { //TODO use error fields on TextView for errors
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


}