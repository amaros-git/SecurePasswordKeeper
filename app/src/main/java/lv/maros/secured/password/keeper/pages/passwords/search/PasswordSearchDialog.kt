package lv.maros.secured.password.keeper.pages.passwords.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private lateinit var binding: DialogPasswordSearchBinding

    private lateinit var passwordListAdapter: PasswordsSearchAdapter

    private fun configurePasswordRecyclerView() {
        val adapter = PasswordsSearchAdapter()
        //TODO rework setup extension to <T>
        binding.searchDialogSuggestionsList.apply {
            layoutManager = LinearLayoutManager(this.context)
            this.adapter = adapter
        }

        adapter.submitMyList(passwords)
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

        configurePasswordRecyclerView()

        return binding.root
    }

    companion object {

        const val PASSWORD_SEARCH_DIALOG_TAG = "PASSWORD_SEARCH_TAG"

        fun newInstance(passwords: List<Password>): PasswordSearchDialog {
            return PasswordSearchDialog(passwords)
        }
    }
}