package lv.maros.secured.password.keeper.pages.passwords

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lv.maros.secured.password.keeper.databinding.PasswordItemBinding
import lv.maros.secured.password.keeper.models.Password
import timber.log.Timber

class PasswordListAdapter : ListAdapter<Password, PasswordViewHolder>(PasswordDiffCallback()) {
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PasswordViewHolder.from(parent)


    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        val password: Password = getItem(position)
        holder.bind(password)

    }

    public override fun getItem(position: Int): Password {
        return super.getItem(position)
    }

    fun submitMyList(list: List<Password>) {
        adapterScope.launch {
            withContext(Dispatchers.Main) { // runs on Main thread because it updates Recycler View
                submitList(list)
            }
        }
    }

}

class PasswordViewHolder(private val binding: PasswordItemBinding) :
    RecyclerView.ViewHolder(binding.root), View.OnLongClickListener {

    val container: ConstraintLayout = binding.passwordItemLayout //TODO must not depend on ConstrainLayout. Use custom view for password

    init {
        binding.root.setOnLongClickListener(this)
    }

    fun bind(item: Password) {
        binding.password = item
        binding.executePendingBindings()


        binding.passwordItemPasswordText.setOnPasswordVisibilityClickListener {
            Timber.d("visibility status = $it")
        }
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

    override fun onLongClick(v: View): Boolean {
        Toast.makeText(v.context, "long click", Toast.LENGTH_SHORT).show()

        return true
    }
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