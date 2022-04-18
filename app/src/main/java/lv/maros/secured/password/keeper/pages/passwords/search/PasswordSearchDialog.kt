package lv.maros.secured.password.keeper.pages.passwords.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import lv.maros.secured.password.keeper.databinding.DialogPasswordSearchBinding
import lv.maros.secured.password.keeper.helpers.geasture.PasswordItemSwipeCallback
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.pages.passwords.PasswordListAdapter
import lv.maros.secured.password.keeper.utils.setup
import timber.log.Timber


class PasswordSearchDialog private constructor(
    private val passwords: List<Password>

) : BottomSheetDialogFragment() {

    private val viewModel: PasswordSearchViewModel by viewModels {
        PasswordSearchViewModelFactory(requireActivity().application)
    }

    private lateinit var binding: DialogPasswordSearchBinding

    private lateinit var passwordsSearchAdapter: PasswordsSearchAdapter

    private fun configurePasswordRecyclerView() {
        passwordsSearchAdapter = PasswordsSearchAdapter()
        //TODO rework setup extension to <T>
        binding.searchDialogSuggestionsList.apply {
            layoutManager = LinearLayoutManager(this.context)
            this.adapter = adapter
        }

        passwordsSearchAdapter.submitMyList(passwords)
    }

    private fun configureSearchView() {
        //TODO refactor later
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Timber.d("search phrase = $s")

            }
        }

        binding.searchDialogSearchText.addTextChangedListener(textWatcher)
    }

    private fun configureViews() {
        configurePasswordRecyclerView()
        configureSearchView()

        viewModel.searchResult.observe(viewLifecycleOwner) {
            it?.let {
                //passwordsSearchAdapter.submitMyList(it)
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

        const val PASSWORD_SEARCH_DIALOG_TAG = "PASSWORD_SEARCH_TAG"

        fun newInstance(passwords: List<Password>): PasswordSearchDialog {
            return PasswordSearchDialog(passwords)
        }
    }
}