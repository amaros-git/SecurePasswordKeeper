package lv.maros.secured.password.keeper.pages.passwords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.databinding.DialogPasswordSearchBinding
import lv.maros.secured.password.keeper.utils.setDisplayHomeAsUpEnabled
import lv.maros.secured.password.keeper.utils.setTitle


class PasswordSearchDialog private constructor(
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogPasswordSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPasswordSearchBinding.inflate(inflater)
        binding.lifecycleOwner = this.viewLifecycleOwner

        dialog?.setCanceledOnTouchOutside(false)

        return binding.root
    }

    companion object {

        fun newInstance(): PasswordSearchDialog {
            return PasswordSearchDialog()
        }
    }
}