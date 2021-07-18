package lv.maros.keeper.setup.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import lv.maros.keeper.databinding.FragmentSelectAuthMethodBinding
import lv.maros.keeper.setup.SharedSetupViewModel

class SelectAuthMethodFragment : Fragment() {

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
    }

    private fun showPasskeyInputDialog(dialogTag: String) {
        val authType =
            if (dialogTag == PASSWORD_INPUT_TAG) PasskeyInputBottomDialog.AUTH_TYPE_PASSWORD
            else PasskeyInputBottomDialog.AUTH_TYPE_PIN

        val dialogFragment =
            PasskeyInputBottomDialog.newInstance(authType)
        dialogFragment.show(requireActivity().supportFragmentManager, dialogTag)
    }

    companion object {
        private const val PASSWORD_INPUT_TAG = "passwordInputDialog"
        private const val PIN_INPUT_TAG = "pinInputDialog"
    }


}