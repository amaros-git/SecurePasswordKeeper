package lv.maros.secured.password.keeper.pages.passwords

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lv.maros.secured.password.keeper.databinding.PasswordItemBinding
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.views.OnCopyClickListener
import lv.maros.secured.password.keeper.views.OnPasswordClickListener
import timber.log.Timber

class PasswordListAdapter(
    private val passwordClickListener: OnPasswordClickListener,
    private val copyClickListener: OnCopyClickListener
) : ListAdapter<Password, PasswordViewHolder>(PasswordDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    private fun setClickListeners(binding: PasswordItemBinding, position: Int) {
        binding.passwordItemPasswordText.setOnPasswordClickListener(passwordClickListener)

        binding.passwordItemWebsiteCopyButton.setOnCopyClickListener(position, copyClickListener)
        binding.passwordItemUsernameCopyButton.setOnCopyClickListener(position, copyClickListener)
        binding.passwordItemPasswordCopyButton.setOnCopyClickListener(position, copyClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PasswordViewHolder.from(parent)

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        val password: Password = getItem(position)
        setClickListeners(holder.binding, position)
        holder.bind(password)
    }

    public override fun getItem(position: Int): Password {
        return super.getItem(position)
    }

    fun submitMyList(list: List<Password>) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }
}

class PasswordViewHolder(val binding: PasswordItemBinding) :
    RecyclerView.ViewHolder(binding.root)/*, View.OnLongClickListener*/ {

    init {
        //binding.root.setOnLongClickListener(this)
    }

    fun bind(item: Password) {
        binding.password = item
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): PasswordViewHolder {
            val binding = PasswordItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return PasswordViewHolder(binding)
        }
    }

    //At the moment isn't implemented.
    /*override fun onLongClick(v: View): Boolean {
        Timber.d("PasswordItem long click")

        return true
    }*/
}

class PasswordDiffCallback : DiffUtil.ItemCallback<Password>() {
    override fun areItemsTheSame(
        oldItem: Password,
        newItem: Password
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Password,
        newItem: Password
    ): Boolean {
        return oldItem == newItem
    }
}