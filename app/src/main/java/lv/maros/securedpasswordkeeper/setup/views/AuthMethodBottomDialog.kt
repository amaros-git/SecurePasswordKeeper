package lv.maros.securedpasswordkeeper.setup.views

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import lv.maros.securedpasswordkeeper.databinding.DialogAuthMethodBinding
import lv.maros.securedpasswordkeeper.databinding.FragmentSelectAuthMethodBinding
import timber.log.Timber

class AuthMethodBottomDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogAuthMethodBinding

    companion object {
        fun newInstance(): AuthMethodBottomDialog {
            return AuthMethodBottomDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Timber.d("onCreateView called")
        binding = DialogAuthMethodBinding.inflate(inflater)

        return binding.root

    }
}