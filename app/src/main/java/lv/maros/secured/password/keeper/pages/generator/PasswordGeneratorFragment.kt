package lv.maros.secured.password.keeper.pages.generator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import lv.maros.secured.password.keeper.base.BaseFragment
import lv.maros.secured.password.keeper.databinding.FragmentPasswordGeneratorBinding
import lv.maros.secured.password.keeper.utils.setDisplayHomeAsUpEnabled


class PasswordGeneratorFragment : BaseFragment() {

    private lateinit var binding: FragmentPasswordGeneratorBinding

    override val _viewModel: PasswordGeneratorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordGeneratorBinding.inflate(inflater)
        binding.lifecycleOwner = this.viewLifecycleOwner

        setDisplayHomeAsUpEnabled(true)

        return binding.root
    }




}