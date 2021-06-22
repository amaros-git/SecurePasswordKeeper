package lv.maros.securedpasswordkeeper.setup.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import lv.maros.securedpasswordkeeper.databinding.FragmentSelectAuthMethodBinding
import lv.maros.securedpasswordkeeper.setup.SharedSetupViewModel

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
            showPinSetupDialog()
        }

        binding.password.setOnClickListener {
            showPasswordSetupDialog()
        }
    }

    private fun showPasswordSetupDialog() {
        val dialogFragment =
            PasskeyInputBottomDialog.newInstance(viewModel, PasskeyInputBottomDialog.AUTH_TYPE_PASSWORD)
        dialogFragment.show(requireActivity().supportFragmentManager, "passwordInputDialog")
    }

    private fun showPinSetupDialog() {
        val dialogFragment =
            PasskeyInputBottomDialog.newInstance(viewModel, PasskeyInputBottomDialog.AUTH_TYPE_PIN)
        dialogFragment.show(requireActivity().supportFragmentManager, "pinInputDialog")
    }


}