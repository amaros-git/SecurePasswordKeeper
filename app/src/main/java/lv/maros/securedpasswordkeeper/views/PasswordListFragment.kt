package lv.maros.securedpasswordkeeper.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import lv.maros.securedpasswordkeeper.SharedPasswordViewModel
import lv.maros.securedpasswordkeeper.R
import lv.maros.securedpasswordkeeper.databinding.FragmentPasswordListBinding
import lv.maros.securedpasswordkeeper.utils.setTitle

class PasswordListFragment : Fragment() {

    private lateinit var binding: FragmentPasswordListBinding

    private val viewModel: SharedPasswordViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPasswordListBinding.inflate(inflater)
        //binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        setTitle(getString(R.string.app_name))

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        binding.addPassword.setOnClickListener {
            findNavController().navigate(
                PasswordListFragmentDirections.actionPasswordListFragmentToAddPasswordFragment()
            )
        }
    }
}