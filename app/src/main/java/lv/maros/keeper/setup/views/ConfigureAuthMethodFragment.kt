package lv.maros.keeper.setup.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.keeper.databinding.FragmentSelectAuthMethodBinding
import lv.maros.keeper.security.KeeperCryptor
import lv.maros.keeper.setup.SharedSetupViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ConfigureAuthMethodFragment : Fragment() {

    private lateinit var binding: FragmentSelectAuthMethodBinding

    private val viewModel: SharedSetupViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectAuthMethodBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        binding.pin.setOnClickListener {
            showPasskeyInputDialog(PIN_INPUT_TAG)
        }

        binding.password.setOnClickListener {
            showPasskeyInputDialog(PASSWORD_INPUT_TAG)
        }

        binding.disableAuth.setOnClickListener {
            //TODO show snackbar with Yes NO
        }
        viewModel.showToast.observe(viewLifecycleOwner) {
            it?.let {
                showToast(it)
            }
        }
    }

    private fun showPasskeyInputDialog(dialogTag: String) {
        val authType =
            if (dialogTag == PASSWORD_INPUT_TAG) PasskeyInputBottomDialog.AUTH_TYPE_PASSWORD
            else PasskeyInputBottomDialog.AUTH_TYPE_PIN

        PasskeyInputBottomDialog.newInstance(authType)
            .show(requireActivity().supportFragmentManager, dialogTag)
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


    companion object {
        private const val PASSWORD_INPUT_TAG = "passwordInputDialog"
        private const val PIN_INPUT_TAG = "pinInputDialog"

        const val DIALOG_RESULT_KEY = "dialog_result"

        const val PASSKEY_HASH_KEY = "passkey_key"
    }


}