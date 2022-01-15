package lv.maros.secured.password.keeper.pages.passwords

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.databinding.FragmentPasswordsBinding
import timber.log.Timber

import lv.maros.secured.password.keeper.helpers.geasture.PasswordClickListener
import lv.maros.secured.password.keeper.helpers.geasture.PasswordItemSwipeCallback
import lv.maros.secured.password.keeper.pages.modify.PasswordModifyFragment
import lv.maros.secured.password.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.secured.password.keeper.utils.setTitle
import lv.maros.secured.password.keeper.utils.setup


@AndroidEntryPoint
class PasswordsFragment : Fragment() {

    private lateinit var binding: FragmentPasswordsBinding

    private val viewModel: PasswordsViewModel by activityViewModels()

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
            PasswordModifyFragment.MODE_ADD_PASSWORD -> {
                PasswordsFragmentDirections.actionPasswordListFragmentToAddPasswordFragment(
                    mode
                )
            }
            PasswordModifyFragment.MODE_EDIT_PASSWORD -> {
                PasswordsFragmentDirections.actionPasswordListFragmentToAddPasswordFragment(
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
            navigateToAddEditFragment(PasswordModifyFragment.MODE_ADD_PASSWORD)
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

    private val passwordClickListener: PasswordClickListener = object : PasswordClickListener {
        override fun onDeleteClick(swipedPos: Int) {
            Toast.makeText(requireContext(), "DELETE", Toast.LENGTH_SHORT).show()
        }

        override fun onEditClick(swipedPos: Int) {
            navigateToAddEditFragment(
                PasswordModifyFragment.MODE_EDIT_PASSWORD,
                getPasswordId(swipedPos)
            )
        }
    }

    private fun configurePasswordRecyclerView() {
        passwordListAdapter = PasswordListAdapter()

        binding.passwordList.setup(passwordListAdapter)

        ItemTouchHelper(
            PasswordItemSwipeCallback(
                requireContext(),
                binding.passwordList,
                passwordClickListener
            )
        ).attachToRecyclerView(
            binding.passwordList
        )
    }

}