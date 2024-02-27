package lv.maros.secured.password.keeper.pages.passwords

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.window.layout.WindowMetricsCalculator
import com.google.android.material.snackbar.Snackbar
import lv.maros.secured.password.keeper.KeeperApplication
import lv.maros.secured.password.keeper.PASSWORD_REMOVAL_SNACKBAR_DURATION
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.base.BaseFragment
import lv.maros.secured.password.keeper.databinding.FragmentPasswordsBinding
import lv.maros.secured.password.keeper.helpers.geasture.PasswordItemSwipeCallback
import lv.maros.secured.password.keeper.helpers.geasture.PasswordItemSwipeListener
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.models.PasswordSearchResult
import lv.maros.secured.password.keeper.pages.addEdit.PasswordAddEditFragment
import lv.maros.secured.password.keeper.pages.passwords.search.PasswordSearchDialog
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

    private fun configureBottomNavigation() {
        binding.passwordsBottomMenu.apply {
            uncheckAllItems()
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.searchMenu -> {
                        processSearchMenuItemClick()

                        true
                    }
                    R.id.sortMenu -> {
                        toggleViewVisibility(binding.passwordsSortChips)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun processSearchMenuItemClick() {
        if (passwordListAdapter.isSearchResultsFilterActive()) {
            passwordListAdapter.clearAllFilters()
            changeSearchMenuIcon("search")
        } else {
            showSearchDialog()
        }
    }

    private fun showSearchDialog() {
        val passwords = _viewModel.getPasswordsList()

        if (null != passwords) {
            PasswordSearchDialog.newInstance(passwords)
                .show(
                    requireActivity().supportFragmentManager,
                    PasswordSearchDialog.PASSWORD_SEARCH_DIALOG_TAG
                )
        } else {
            Toast.makeText(
                requireContext(),
                "No passwords to search in",
                Toast.LENGTH_SHORT
            ).show()
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

    private fun configureViews() {
        binding.passwordsAddPasswordFab.setOnClickListener {
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

        binding.passwordsPasswordList.setup(passwordListAdapter)

        //binding.passwordList.isNestedScrollingEnabled = false

        ItemTouchHelper(
            PasswordItemSwipeCallback(
                requireContext(),
                passwordItemClickListener
            )
        ).attachToRecyclerView(
            binding.passwordsPasswordList
        )
    }

    private fun setSearchDialogResultListener() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            PasswordSearchDialog.PASSWORD_SEARCH_REQUEST_TAG,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            if (PasswordSearchDialog.PASSWORD_SEARCH_REQUEST_TAG == requestKey) {
                bundle.getParcelableArray(PasswordSearchDialog.PASSWORD_SEARCH_RESULT_TAG)?.let {
                    processSearchDialogResult(it.filterIsInstance<PasswordSearchResult>())
                }
            }
        }
    }

    private fun processSearchDialogResult(searchResultItems: List<PasswordSearchResult>) {
        passwordListAdapter.showSearchResultItems(searchResultItems)

        changeSearchMenuIcon("clear")
    }

    private fun changeSearchMenuIcon(type: String) {
        val searchMenuItem = binding.passwordsBottomMenu.menu.findItem(R.id.searchMenu)
        when (type) {
            "search" -> {
                searchMenuItem.icon = ResourcesCompat.getDrawable(
                    activity?.resources!!,
                    R.drawable.ic_baseline_search_18,
                    null
                )
                searchMenuItem.title = "Search" //TODO
            }
            "clear" -> {
                searchMenuItem.icon = ResourcesCompat.getDrawable(
                    activity?.resources!!,
                    R.drawable.ic_baseline_cancel_18,
                    null
                )
                searchMenuItem.title = "Clear" //TODO
            }
        }
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
    private fun showUndoPasswordRemoval(
        password: Password,
        swipedPos: Int,
        workRequestTag: String
    ) {
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
            .setAnchorView(binding.passwordsBottomMenu)
            .show()
    }

    private fun undoPasswordRemoval(password: Password, swipedPos: Int, workRequestTag: String) {
        _viewModel.undoPasswordsRemoval(passwordListAdapter, password, swipedPos, workRequestTag)
        binding.passwordsPasswordList.layoutManager?.scrollToPosition(swipedPos)
    }

    //TODO rework
    private fun configureSortChips() {
        binding.passwordsSortChips.setOnClickListener { //TODO wtf ?
            Timber.d("Clicked")
        }

        //by design only one chip can be checked.
        binding.passwordsSortChips.setOnCheckedStateChangeListener { _, checkedIds ->
            checkedIds.forEach {
                when (it) {
                    R.id.username_AZ_sort_chip -> {
                        passwordListAdapter.sortPasswords(PasswordListAdapter.SORTING_TYPE_USERNAME_AZ)
                    }
                    R.id.username_ZA_sort_chip -> {
                        passwordListAdapter.sortPasswords(PasswordListAdapter.SORTING_TYPE_USERNAME_ZA)
                    }
                    R.id.website_AZ_sort_chip -> {
                        passwordListAdapter.sortPasswords(PasswordListAdapter.SORTING_TYPE_WEBSITE_AZ)
                    }
                    R.id.website_ZA_sort_chip -> {
                        passwordListAdapter.sortPasswords(PasswordListAdapter.SORTING_TYPE_WEBSITE_ZA)
                    }
                    R.id.latest_sort_chip -> {
                        passwordListAdapter.sortPasswords(PasswordListAdapter.SORTING_TYPE_LATEST)
                    }
                    R.id.oldest_sort_chip -> {
                        passwordListAdapter.sortPasswords(PasswordListAdapter.SORTING_TYPE_OLDEST)
                    }
                }
            }
        }
    }

    enum class WindowSizeClass { COMPACT, MEDIUM, EXPANDED }

    private fun computeWindowSizeClasses() {
        val metrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(getApplicationContext())

        val widthDp = metrics.bounds.width() /
                resources.displayMetrics.density
        val widthWindowSizeClass = when {
            widthDp < 600f -> WindowSizeClass.COMPACT
            widthDp < 840f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }

        val heightDp = metrics.bounds.height() /
                resources.displayMetrics.density
        val heightWindowSizeClass = when {
            heightDp < 480f -> WindowSizeClass.COMPACT
            heightDp < 900f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }

        // Use widthWindowSizeClass and heightWindowSizeClass.
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPasswordsBinding.inflate(inflater)
        binding.viewModel = _viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setTitle(getString(R.string.app_name))
        setDisplayHomeAsUpEnabled(false)

        //TODO rework config methods
        configureViews()
        configurePasswordRecyclerView()
        configureBottomNavigation()
        setSearchDialogResultListener()
        configureSortChips()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        _viewModel.loadAllPasswords() // When return from AddEdit, we need to update passwords. TODO can avoid to read all passwords from DB ?
    }

}