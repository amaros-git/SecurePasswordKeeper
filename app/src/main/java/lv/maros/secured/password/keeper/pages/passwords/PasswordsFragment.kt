package lv.maros.secured.password.keeper.pages.passwords

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import lv.maros.secured.password.keeper.KeeperApplication
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.databinding.FragmentPasswordsBinding
import lv.maros.secured.password.keeper.databinding.PasswordItemBinding
import timber.log.Timber

import lv.maros.secured.password.keeper.helpers.geasture.PasswordItemClickListener
import lv.maros.secured.password.keeper.helpers.geasture.PasswordItemSwipeCallback
import lv.maros.secured.password.keeper.pages.addEdit.PasswordAddEditFragment
import lv.maros.secured.password.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.secured.password.keeper.utils.setTitle
import lv.maros.secured.password.keeper.utils.setup
import lv.maros.secured.password.keeper.views.OnPasswordClickListener
import lv.maros.secured.password.keeper.views.PasswordTextView


class PasswordsFragment : Fragment() {

    private lateinit var binding: FragmentPasswordsBinding

    private val viewModel: PasswordsViewModel by viewModels {
        PasswordsViewModelFactory(
            (requireContext().applicationContext as KeeperApplication).localPasswordsRepository,
            (requireContext().applicationContext as KeeperApplication).cryptor,
            (requireContext().applicationContext as KeeperApplication)
        )
    }

    private lateinit var passwordListAdapter: PasswordListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPasswordsBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        setTitle(getString(R.string.app_name))
        setDisplayHomeAsUpEnabled(false)

        configurePasswordRecyclerView()
        setupViews()
        setupBottomNavigation()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadAllPasswords()
    }

    private fun setupBottomNavigation() {
        //I don't need to check any item. This just like a button.
        binding.bottomNavigation.menu.setGroupCheckable(0, false, false)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.settingsMenu -> {
                    Timber.d("SETTINGS")
                    true
                }
                R.id.sortMenu -> {
                    Timber.d("SORT")
                    true
                }
                else -> false
            }
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

        if (null != action) {
            findNavController().navigate(action)
        } else {
            //TODO
        }

    }

    private fun setupViews() {
        binding.addPasswordFab.setOnClickListener {
            navigateToAddEditFragment(PasswordAddEditFragment.MODE_ADD_PASSWORD)
        }

        viewModel.passwordList.observe(viewLifecycleOwner) {
            it.forEach { password ->
                Timber.d(password.toString())
            }

            it?.let {
                passwordListAdapter.submitMyList(it)
            }
        }
    }

    private fun getPasswordId(swipedPos: Int): Int {
        val password = passwordListAdapter.getItem(swipedPos)
        Timber.d("passwordId = ${password.id}")
        return password.id
    }

    private val passwordItemClickListener: PasswordItemClickListener = object : PasswordItemClickListener {
        override fun onDeleteClick(swipedPos: Int) {
            Toast.makeText(requireContext(), "DELETE", Toast.LENGTH_SHORT).show()
        }

        override fun onEditClick(swipedPos: Int) {
            navigateToAddEditFragment(
                PasswordAddEditFragment.MODE_EDIT_PASSWORD,
                getPasswordId(swipedPos)
            )
        }
    }

    private fun configurePasswordRecyclerView() {
        val copyClickListener = View.OnClickListener {
            processCopyClick(it)
        }

        val passwordVisibilityClickListener: OnPasswordClickListener =
            { b: Boolean, s: String ->
                processPasswordVisibilityClick(b, s)
            }

        passwordListAdapter =
            PasswordListAdapter(passwordVisibilityClickListener, copyClickListener)

        binding.passwordList.setup(passwordListAdapter)

        ItemTouchHelper(
            PasswordItemSwipeCallback(
                requireContext(),
                binding.passwordList,
                passwordItemClickListener
            )
        ).attachToRecyclerView(
            binding.passwordList
        )
    }

    private fun processCopyClick(view: View) {
        val clipboard = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val text = getClickedPasswordItemViewText(view)
        Timber.d("copy text = $text")
        clipboard.setPrimaryClip(
            ClipData.newPlainText(
                "SecurePasswordKeeper",
                text
            )
        )
    }

    private fun getClickedPasswordItemViewText(view: View): String =
        when (view.id) {
            R.id.passwordItem_website_copy_button ->
                (getView()?.findViewById(R.id.passwordItem_website_text) as TextView).text.toString()
            R.id.passwordItem_username_copy_button ->
                (getView()?.findViewById(R.id.passwordItem_username_text) as TextView).text.toString()
            R.id.passwordItem_password_copy_button ->
                (getView()?.findViewById(R.id.passwordItem_password_text) as PasswordTextView).text.toString()
            else -> ""
        }

    //TODO remove unused isVisible
    private fun processPasswordVisibilityClick(isVisible: Boolean, data: String) =
        viewModel.decryptString(data)


}