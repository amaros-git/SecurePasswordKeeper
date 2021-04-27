package lv.maros.securedpasswordkeeper

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import lv.maros.securedpasswordkeeper.databinding.PasswordListFragmentBinding
import lv.maros.securedpasswordkeeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.securedpasswordkeeper.utils.setTitle

class PasswordListFragment : Fragment() {

    private lateinit var binding: PasswordListFragmentBinding

    private lateinit var viewModel: PasswordListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = PasswordListFragmentBinding.inflate(inflater)
        //binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        setTitle(getString(R.string.app_name))

        return binding.root
    }
}