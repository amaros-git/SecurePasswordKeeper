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

        binding = FragmentPasswordListBinding.inflate(inflater).also {
            //binding.viewModel = viewModel
            it.lifecycleOwner = this.viewLifecycleOwner
        }

        setTitle(getString(R.string.app_name))
        setDisplayHomeAsUpEnabled(false)

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        binding.addPassword.setOnClickListener {
            findNavController().navigate(
                PasswordListFragmentDirections.actionPasswordListFragmentToAddPasswordFragment()
            )
        }

        viewModel.passwordList.observe(viewLifecycleOwner) {
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