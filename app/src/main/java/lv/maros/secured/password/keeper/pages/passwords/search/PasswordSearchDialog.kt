package lv.maros.secured.password.keeper.pages.passwords.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import lv.maros.secured.password.keeper.databinding.DialogPasswordSearchBinding
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.models.PasswordSearchResult
import lv.maros.secured.password.keeper.views.OnPasswordSearchClickListener
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
        val clickListener: OnPasswordSearchClickListener = { pos ->
            processOnSearchSuggestionItemClick(pos)
        }

        passwordsSearchAdapter = PasswordsSearchAdapter(clickListener)

        binding.searchDialogSuggestionsList.apply {
            layoutManager = LinearLayoutManager(this.context)
            this.adapter = passwordsSearchAdapter
        }
    }

    private fun processOnSearchSuggestionItemClick(position: Int) =
        setSearchResultAndFinish(arrayOf(passwordsSearchAdapter.getItem(position)))

    private fun processOnSearchButtonClick() =
        setSearchResultAndFinish(passwordsSearchAdapter.currentList.toTypedArray())

    private fun setSearchResultAndFinish(searchItems: Array<PasswordSearchResult>) {
        requireActivity().supportFragmentManager.setFragmentResult(
            PASSWORD_SEARCH_REQUEST_TAG,
            bundleOf(PASSWORD_SEARCH_RESULT_TAG to searchItems)
        )

        dismiss()
    }

    private fun configureSearchView() {
        //TODO refactor later
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    viewModel.clearSearchSuggestions()
                } else {
                    viewModel.showSearchSuggestions(s)
                }
            }
        }

        binding.searchDialogSearchText.addTextChangedListener(textWatcher)

        binding.searchDialogShowAllButton.setOnClickListener {
            processOnSearchButtonClick()
        }
    }

    private fun configureViews() {
        configurePasswordRecyclerView()
        configureSearchView()

        viewModel.searchResult.observe(viewLifecycleOwner) {
            it?.let {
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

        const val PASSWORD_SEARCH_REQUEST_TAG = "PASSWORD_SEARCH_REQUEST_TAG"

        //array of PasswordSearchResult. If nothing found, empty array is returned.
        const val PASSWORD_SEARCH_RESULT_TAG = "PASSWORD_SEARCH_RESULT_TAG"

        fun newInstance(passwords: List<Password>): PasswordSearchDialog {
            return PasswordSearchDialog(passwords)
        }
    }
}