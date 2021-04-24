package lv.maros.securedpasswordkeeper

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class PasswordListFragment : Fragment() {

    companion object {
        fun newInstance() = PasswordListFragment()
    }

    private lateinit var viewModel: PasswordListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.password_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PasswordListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}