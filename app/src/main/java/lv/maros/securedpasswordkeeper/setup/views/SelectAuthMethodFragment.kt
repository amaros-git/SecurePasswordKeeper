package lv.maros.securedpasswordkeeper.setup.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories.getFragmentFactory
import lv.maros.securedpasswordkeeper.R
import lv.maros.securedpasswordkeeper.databinding.FragmentSelectAuthMethodBinding

class SelectAuthMethodFragment : Fragment() {

    private lateinit var binding: FragmentSelectAuthMethodBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSelectAuthMethodBinding.inflate(inflater).also {
            it.lifecycleOwner = viewLifecycleOwner
        }

        binding.pin.setOnClickListener {
            showPinSetupDialog()
        }

        return binding.root
    }

    private fun showPinSetupDialog() {
        val dialogFragment = AuthMethodBottomDialog.newInstance()
        dialogFragment.show(requireActivity().supportFragmentManager, "opa")

    }

}