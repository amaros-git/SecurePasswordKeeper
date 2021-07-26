package lv.maros.keeper.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lv.maros.keeper.databinding.PasswordItemBinding
import lv.maros.keeper.models.Password

class PasswordListAdapter() : ListAdapter<Password, PasswordViewHolder>(PasswordDiffCallback()) {
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

    fun submitMyList(list: List<Password>, headerText: String? = null) {
        /*val job = adapterScope.launch {
            val items = if (null != headerText) {
                listOf(ElectionDataItem.Header(headerText)) + list.map {
                    ElectionDataItem.ElectionItem(it)
                }
            } else {
                list.map { ElectionDataItem.ElectionItem(it) }
            }

            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }*/
    }

}

class PasswordViewHolder(private val binding: PasswordItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
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