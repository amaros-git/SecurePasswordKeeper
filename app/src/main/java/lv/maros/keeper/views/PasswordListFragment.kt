package lv.maros.keeper.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import lv.maros.keeper.SharedKeeperViewModel
import lv.maros.keeper.databinding.FragmentPasswordListBinding
import lv.maros.keeper.utils.*
import timber.log.Timber

import lv.maros.keeper.R
import lv.maros.keeper.helpers.geasture.PasswordItemSwipeCallback


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


    private fun setupViews() {
        binding.addPassword.setOnClickListener {
            findNavController().navigate(
                PasswordListFragmentDirections.actionPasswordListFragmentToAddPasswordFragment()
            )
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

    private fun configurePasswordListView() {
        passwordListAdapter = PasswordListAdapter()
        binding.passwordList.setup(passwordListAdapter)

        /*binding.passwordList.addOnItemTouchListener(
            RecyclerTouchListener(
                requireContext(),
                binding.passwordList,
                object : ClickListener {

                    override fun onClick(view: View, position: Int) {
                        Timber.d("OnClick")
                    }

                    override fun onLongClick(view: View, position: Int) {
                        //TODO I shall create custom layout for Password Item
                        Timber.d("OnLongClick")
                        *//*if (view is CardView) {
                            view.findViewById<CheckBox>(R.id.itemSelectBox).apply {
                                visibility = View.VISIBLE
                            }

                        }*//*
                    }
                })
        )*/

        ItemTouchHelper(
            PasswordItemSwipeCallback(
                requireContext(),
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                binding.passwordList
            )
        ).attachToRecyclerView(
            binding.passwordList
        )
/*
        ItemTouchHelper(swipeCallback).attachToRecyclerView(
            binding.passwordList
        )*/


        /*val swipeCallback: SwipeHelper = object : SwipeHelper(requireContext(), binding.passwordList) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>?
            ) {
                underlayButtons?.add(SwipeHelper.UnderlayButton(
                    "Test",
                    0,
                    Color.GREEN
                ) { Toast.makeText(requireContext(), "Click", Toast.LENGTH_SHORT).show() })
            }
        }

        ItemTouchHelper(swipeCallback).attachToRecyclerView(
            binding.passwordList)*/
    }

}