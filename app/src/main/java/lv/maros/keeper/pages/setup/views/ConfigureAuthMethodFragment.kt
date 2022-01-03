package lv.maros.keeper.pages.setup.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.keeper.databinding.FragmentSelectAuthMethodBinding
import lv.maros.keeper.pages.setup.SharedSetupViewModel
import lv.maros.keeper.utils.KEEPER_AUTH_TYPE_PASSKEY
import lv.maros.keeper.utils.KEEPER_AUTH_TYPE_PIN

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
            showPasskeyInputDialog(KEEPER_AUTH_TYPE_PIN)
        }

        binding.password.setOnClickListener {
            showPasskeyInputDialog(KEEPER_AUTH_TYPE_PASSKEY)
        }

        binding.disableAuth.setOnClickListener {
            //TODO show snackbar with Yes NO
        }

        viewModel.showToastEvent.observe(viewLifecycleOwner) {
            it?.let {
                showToast(it)
            }
        }

        viewModel.authenticationIsConfiguredEvent.observe(viewLifecycleOwner) {
            it?.let {
                findNavController()
                    .navigate(
                        ConfigureAuthMethodFragmentDirections
                            .actionSelectAuthMethodFragmentToFinishSetupFragment()
                    )
            }
        }
    }

    private fun showPasskeyInputDialog(keeperAuthType: String) {
        PasskeyInputBottomDialog.newInstance(keeperAuthType)
            .show(requireActivity().supportFragmentManager, keeperAuthType)
    }

    private fun showToast(msg: String) { //TODO use error fields on TextView for errors
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}