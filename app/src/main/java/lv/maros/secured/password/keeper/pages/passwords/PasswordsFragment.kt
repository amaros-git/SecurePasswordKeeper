package lv.maros.secured.password.keeper.pages.passwords

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
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
                    R.id.addMenu -> {
                        navigateToAddEditFragment(PasswordAddEditFragment.MODE_ADD_PASSWORD)
                        true
                    }
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

    private fun showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(
            requireContext(),
            message,
            length
        ).show()
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
        _viewModel.passwordList.observe(viewLifecycleOwner) {
            it?.let {
                passwordListAdapter.submitMyList(it)
            }
        }
    }

    private fun getPasswordId(swipedPos: Int) = passwordListAdapter.getItem(swipedPos).id

    private val passwordSwipeClickListener =
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

    private fun processPasswordVisibilityClick(data: String) =
        _viewModel.decryptString(data)

    private fun configurePasswordRecyclerView() {
        passwordListAdapter =
            PasswordListAdapter(
                passwordClickListener = { password ->
                    processPasswordVisibilityClick(password)
                },
                passwordLongClickListener = { encryptedPassword ->
                    _viewModel.copyToClipboard(_viewModel.decryptString(encryptedPassword))
                   //showToast(getString(R.string.data_copied))
                },
                textCopyClickListener = { view ->
                    _viewModel.copyToClipboard((view as TextView).text.toString())
                    //showToast(getString(R.string.data_copied))
                    true
                }
            )

        binding.passwordsPasswordList.setup(passwordListAdapter)

        ItemTouchHelper(
            PasswordItemSwipeCallback(
                requireContext(),
                passwordSwipeClickListener
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
                    R.drawable.ic_search,
                    null
                )
                searchMenuItem.title = getString(R.string.search_hint)
            }

            "clear" -> {
                searchMenuItem.icon = ResourcesCompat.getDrawable(
                    activity?.resources!!,
                    R.drawable.ic_cancel,
                    null
                )
                searchMenuItem.title = getString(R.string.clear_text)
            }
        }
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
        binding.passwordsSortChips.setOnClickListener { //TODO ?
            Timber.d("passwordsSortChips clicked")
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DetailedDrawerExample(
        content: @Composable (PaddingValues) -> Unit
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(Modifier.height(12.dp))
                        Text("Drawer Title", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                        HorizontalDivider()

                        Text("Section 1", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                        NavigationDrawerItem(
                            label = { Text("Item 1") },
                            selected = false,
                            onClick = { /* Handle click */ }
                        )
                        NavigationDrawerItem(
                            label = { Text("Item 2") },
                            selected = false,
                            onClick = { /* Handle click */ }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Text("Section 2", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                        NavigationDrawerItem(
                            label = { Text("Settings") },
                            selected = false,
                            icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                            badge = { Text("20") }, // Placeholder
                            onClick = { /* Handle click */ }
                        )
                        NavigationDrawerItem(
                            label = { Text("Help and feedback") },
                            selected = false,
                            icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                            onClick = { /* Handle click */ },
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
            },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Navigation Drawer Example") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) {
                                        drawerState.open()
                                    } else {
                                        drawerState.close()
                                    }
                                }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                }
            ) { innerPadding ->
                content(innerPadding)
            }
        }
    }

    @Composable
    fun NavigationDrawerExamples() {
        // Add more examples here in future if necessary.

        DetailedDrawerExample { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Text(
                    "Swipe from left edge or use menu icon to open the dismissible drawer",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
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

        //NavigationDrawerExamples()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        _viewModel.loadAllPasswords() // When return from AddEdit, we need to update passwords. TODO can avoid to read all passwords from DB ?
    }

}