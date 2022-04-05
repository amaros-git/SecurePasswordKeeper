package lv.maros.secured.password.keeper.pages.passwords

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import lv.maros.secured.password.keeper.KeeperApplication
import lv.maros.secured.password.keeper.PASSWORD_REMOVAL_SNACKBAR_DURATION
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.base.BaseFragment
import lv.maros.secured.password.keeper.databinding.FragmentPasswordsBinding
//import lv.maros.secured.password.keeper.helpers.geasture.PasswordItemClickListener
import lv.maros.secured.password.keeper.helpers.geasture.PasswordItemSwipeCallback
import lv.maros.secured.password.keeper.helpers.geasture.PasswordItemSwipeListener
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.pages.addEdit.PasswordAddEditFragment
import lv.maros.secured.password.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.secured.password.keeper.utils.setTitle
import lv.maros.secured.password.keeper.utils.setup
import lv.maros.secured.password.keeper.utils.uncheckAllItems
import lv.maros.secured.password.keeper.views.OnCopyClickListener
import lv.maros.secured.password.keeper.views.OnPasswordClickListener
import timber.log.Timber


class PasswordsFragment : BaseFragment() {

    private lateinit var binding: FragmentPasswordsBinding

    override val _viewModel: PasswordsViewModel by viewModels {
        PasswordsViewModelFactory(
            (requireContext().applicationContext as KeeperApplication).localPasswordsRepository,
            (requireContext().applicationContext as KeeperApplication).cryptor,
            (requireContext().applicationContext as KeeperApplication)
        )
    }

    private lateinit var passwordListAdapter: PasswordListAdapter

    private fun setupBottomNavigation() {
        binding.bottomNavigation.uncheckAllItems()

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.searchMenu -> {
                    Timber.d("Search")
                    toggleViewVisibility(binding.addEditSearchView)
                    true
                }
                R.id.sortMenu -> {
                    Timber.d("SORT")
                    toggleViewVisibility(binding.addEditSortChips)
                    true
                }
                else -> false
            }
        }
    }

    private fun toggleViewVisibility(view: View) {
        if (view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    private fun navigateToAddEditFragment(mode: Int, passwordId: Int = -1) {
        val action = when (mode) {
            PasswordAddEditFragment.MODE_ADD_PASSWORD -> {
                PasswordsFragmentDirections.actionPasswordsFragmentToPasswordAddEditFragment(
                    mode
                )
            }
            PasswordAddEditFragment.MODE_EDIT_PASSWORD -> {
                PasswordsFragmentDirections.actionPasswordsFragmentToPasswordAddEditFragment(
                    mode,
                    passwordId
                )
            }
            else -> {
                Timber.e("Wrong mode provided for AddEditFragment")
                null
            }
        }

        action?.let { findNavController().navigate(action) }
    }

    private fun setupViews() {
        binding.addPasswordFab.setOnClickListener {
            navigateToAddEditFragment(PasswordAddEditFragment.MODE_ADD_PASSWORD)
        }

        _viewModel.passwordList.observe(viewLifecycleOwner) {
            it?.let {
                passwordListAdapter.submitMyList(it)
            }
        }
    }

    private fun getPasswordId(swipedPos: Int) = passwordListAdapter.getItem(swipedPos).id

    private val passwordItemClickListener =
        object : PasswordItemSwipeListener {

            override fun onSwipeLeft(swipedPos: Int) {
                processPasswordRemoval(swipedPos)
            }

            override fun onSwipeRight(swipedPos: Int) {
                navigateToAddEditFragment(
                    PasswordAddEditFragment.MODE_EDIT_PASSWORD,
                    getPasswordId(swipedPos)
                )
            }
        }

    private fun configurePasswordRecyclerView() {
        val copyClickListener: OnCopyClickListener =
            { view, position ->
                processCopyClick(view, position)
            }

        val passwordVisibilityClickListener: OnPasswordClickListener =
            { s: String ->
                processPasswordVisibilityClick(s)
            }

        passwordListAdapter =
            PasswordListAdapter(passwordVisibilityClickListener, copyClickListener)

        binding.passwordList.setup(passwordListAdapter)

        //binding.passwordList.isNestedScrollingEnabled = false

        ItemTouchHelper(
            PasswordItemSwipeCallback(
                requireContext(),
                passwordItemClickListener
            )
        ).attachToRecyclerView(
            binding.passwordList
        )
    }

    private fun processPasswordVisibilityClick(data: String) =
        _viewModel.decryptString(data)

    private fun processCopyClick(view: View, position: Int) {
        val textToCopy =
            getClickedPasswordItemViewText(view.id, passwordListAdapter.getItem(position))

        val clipboard = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(
            ClipData.newPlainText(
                "SecurePasswordKeeper",
                textToCopy
            )
        )
    }

    private fun getClickedPasswordItemViewText(viewId: Int, password: Password): String =
        when (viewId) {
            R.id.passwordItem_website_copy_button -> password.website
            R.id.passwordItem_username_copy_button -> password.username
            R.id.passwordItem_password_copy_button ->
                _viewModel.decryptString(password.encryptedPassword)
            else -> ""
        }

    private fun processPasswordRemoval(swipedPos: Int) {
        val passwordToDelete = passwordListAdapter.getItem(swipedPos)
        val workRequestTag =
            _viewModel.deletePasswords(
                passwordListAdapter,
                swipedPos,
                intArrayOf(passwordToDelete.id)
            )

        showUndoPasswordRemoval(passwordToDelete, swipedPos, workRequestTag)
    }

    @SuppressLint("WrongConstant")
    private fun showUndoPasswordRemoval(password: Password, swipedPos: Int, workRequestTag: String) {
        Snackbar.make(
            binding.root,
            getString(R.string.password_is_removed), PASSWORD_REMOVAL_SNACKBAR_DURATION
        ).setAction(getString(R.string.undo_password_removal)) {
            undoPasswordRemoval(
                password,
                swipedPos,
                workRequestTag
            )
        }
            .setDuration(PASSWORD_REMOVAL_SNACKBAR_DURATION)
            .setAnchorView(binding.bottomNavigation)
            .show()
    }

    private fun undoPasswordRemoval(password: Password, swipedPos: Int, workRequestTag: String) {
        _viewModel.undoPasswordsRemoval(passwordListAdapter, password, swipedPos, workRequestTag)
        binding.passwordList.layoutManager?.scrollToPosition(swipedPos)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPasswordsBinding.inflate(inflater)
        binding.viewModel = _viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarLayout.keeperToolbar)
        setTitle(getString(R.string.app_name))
        setDisplayHomeAsUpEnabled(false)

        //TODO rework config methods
        configurePasswordRecyclerView()
        setupViews()
        setupBottomNavigation()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        _viewModel.loadAllPasswords() //TODO should it be here ?
    }

}