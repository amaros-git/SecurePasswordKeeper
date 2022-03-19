package lv.maros.secured.password.keeper.pages.generator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.base.BaseFragment
import lv.maros.secured.password.keeper.databinding.FragmentPasswordGeneratorBinding
import lv.maros.secured.password.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.secured.password.keeper.utils.setTitle


class PasswordGeneratorFragment : BaseFragment() {

    private lateinit var binding: FragmentPasswordGeneratorBinding

    override val _viewModel: PasswordGeneratorViewModel by viewModels()


    private fun configureViews() {
        binding.passwordGeneratorCancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordGeneratorBinding.inflate(inflater)
        binding.lifecycleOwner = this.viewLifecycleOwner

        setTitle(getString(R.string.password_generator_fragment_title))
        setDisplayHomeAsUpEnabled(true)

        configureViews()

        return binding.root
    }

}