package lv.maros.keeper.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.keeper.SharedKeeperViewModel
import lv.maros.keeper.R
import lv.maros.keeper.databinding.FragmentPasswordListBinding
import lv.maros.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.keeper.utils.setTitle

@AndroidEntryPoint
class PasswordListFragment : Fragment() {

    private lateinit var binding: FragmentPasswordListBinding

    private val viewModel: SharedKeeperViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPasswordListBinding.inflate(inflater).also {
            //binding.viewModel = viewModel
            it.lifecycleOwner = this.viewLifecycleOwner
        }

        setTitle(getString(R.string.app_name))
        setDisplayHomeAsUpEnabled(false)

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