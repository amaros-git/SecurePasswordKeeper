package lv.maros.keeper.setup.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import lv.maros.keeper.R
import lv.maros.keeper.databinding.FragmentFinishSetupBinding
import lv.maros.keeper.databinding.FragmentSelectAuthMethodBinding
import lv.maros.keeper.setup.SharedSetupViewModel

class FinishSetupFragment : Fragment() {

    private lateinit var binding: FragmentFinishSetupBinding

    private val viewModel: SharedSetupViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishSetupBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        binding.save.setOnClickListener {
            viewModel.finishSetup()
        }
    }
}