package lv.maros.secured.password.keeper.pages.passwords.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import lv.maros.secured.password.keeper.databinding.DialogPasswordSearchBinding
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.views.OnSearchItemClickListener
import timber.log.Timber


class PasswordSearchDialog private constructor(
    private val passwords: List<Password>

) : BottomSheetDialogFragment() {

    private val viewModel: PasswordSearchViewModel by viewModels {
        PasswordSearchViewModelFactory(passwords, requireActivity().application)
    }

    private lateinit var binding: DialogPasswordSearchBinding

    private lateinit var passwordsSearchAdapter: PasswordsSearchAdapter

    private fun configurePasswordRecyclerView() {
        val clickListener: OnSearchItemClickListener = { position ->
            processOnSearchResultItemClick(position)
        }

        passwordsSearchAdapter = PasswordsSearchAdapter(clickListener)
        //TODO rework setup extension to <T>
        binding.searchDialogSuggestionsList.apply {
            layoutManager = LinearLayoutManager(this.context)
            this.adapter = passwordsSearchAdapter
        }
    }

    private fun processOnSearchResultItemClick(position: Int) {
        Timber.d("Clicked on position $position")
        val searchItem = passwordsSearchAdapter.getItem(position)

        requireActivity().supportFragmentManager.setFragmentResult(PASSWORD_SEARCH_REQUEST_TAG, bundleOf("dasdad" to "Hi"))
    }

    private fun configureSearchView() {
        //TODO refactor later
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Timber.d("search phrase = $s")
                if (s.isNullOrEmpty()) {
                    viewModel.clearSearchSuggestions()
                } else {
                    viewModel.showSearchSuggestions(s)
                }
            }
        }

        binding.searchDialogSearchText.addTextChangedListener(textWatcher)
    }

    private fun configureViews() {
        configurePasswordRecyclerView()
        configureSearchView()

        viewModel.searchResult.observe(viewLifecycleOwner) {
            it?.let {
                it.forEach { item ->
                    Timber.d(item.toString())
                }
                passwordsSearchAdapter.submitMyList(it)
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPasswordSearchBinding.inflate(inflater)
        binding.lifecycleOwner = this.viewLifecycleOwner

        dialog?.setCanceledOnTouchOutside(false)

        passwords.forEach {
            Timber.d(it.toString())
        }

        configureViews()

        return binding.root
    }

    companion object {
        const val PASSWORD_SEARCH_DIALOG_TAG = "PASSWORD_SEARCH_DIALOG_TAG"
        const val PASSWORD_SEARCH_REQUEST_TAG = "PASSWORD_SEARCH_RESULT_TAG"

        fun newInstance(passwords: List<Password>): PasswordSearchDialog {
            return PasswordSearchDialog(passwords)
        }
    }
}