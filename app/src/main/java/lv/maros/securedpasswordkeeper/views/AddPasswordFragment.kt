package lv.maros.securedpasswordkeeper.views

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import lv.maros.securedpasswordkeeper.R
import lv.maros.securedpasswordkeeper.databinding.FragmentAddPasswordBinding
import lv.maros.securedpasswordkeeper.utils.setTitle

class AddPasswordFragment : Fragment() {

    private lateinit var binding: FragmentAddPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddPasswordBinding.inflate(inflater)

        binding.lifecycleOwner = this.viewLifecycleOwner

        setTitle(getString(R.string.app_name))

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        val searchManager =
            requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        binding.searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        }
    }

}