package lv.maros.keeper.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.keeper.SharedKeeperViewModel
import lv.maros.keeper.R
import lv.maros.keeper.databinding.FragmentPasswordListBinding
import lv.maros.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.keeper.utils.setTitle
import lv.maros.keeper.utils.setup
import timber.log.Timber

@AndroidEntryPoint
class PasswordListFragment : Fragment() {

    private lateinit var binding: FragmentPasswordListBinding

    private val viewModel: SharedKeeperViewModel by activityViewModels()

    private lateinit var passwordListAdapter: PasswordListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPasswordListBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner


        setTitle(getString(R.string.app_name))
        setDisplayHomeAsUpEnabled(false)

        configurePasswordListView()

        setupViews()

        setupBottomNavigation()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadPassword()
    }

    private fun setupBottomNavigation() {
        //I don't need to check any item. This just like a button.
        binding.bottomNavigation.menu.setGroupCheckable(0, false, false)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
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


    private fun setupViews() {
        binding.addPassword.setOnClickListener {
            findNavController().navigate(
                PasswordListFragmentDirections.actionPasswordListFragmentToAddPasswordFragment()
            )
        }

        viewModel.passwordList.observe(viewLifecycleOwner) {
            it.forEach {password ->
                Timber.d(password.toString())
            }

            it?.let {
                passwordListAdapter.submitMyList(it)
            }
        }
    }

    private fun configurePasswordListView() {
        passwordListAdapter = PasswordListAdapter()
        binding.passwordList.setup(passwordListAdapter)


        val itemTouchCallback = ItemTouchHelper(reminderListItemTouchCallback)
        itemTouchCallback.attachToRecyclerView(binding.passwordList)
    }

    private var reminderListItemTouchCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val password = passwordListAdapter.getItem(position)
                Timber.d("performed swipe on $password")
            }
        }
}