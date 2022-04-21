package lv.maros.secured.password.keeper.pages.passwords.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lv.maros.secured.password.keeper.databinding.PasswordItemBinding
import lv.maros.secured.password.keeper.databinding.PasswordSearchItemBinding
import lv.maros.secured.password.keeper.models.PasswordSearchResultItem

class PasswordsSearchAdapter(private val clickListener) : ListAdapter<PasswordSearchResultItem, PasswordViewHolder>(PasswordDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PasswordViewHolder.from(parent)

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        val passwordItem = getItem(position)
        holder.bind(passwordItem)
    }

    /*  public override fun getItem(position: Int): Password {
          return super.getItem(position)
      }*/

    fun submitMyList(list: List<PasswordSearchResultItem>) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }
}

class PasswordViewHolder(val binding: PasswordSearchItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PasswordSearchResultItem) {
        binding.searchItem = item
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): PasswordViewHolder {
            val binding = PasswordSearchItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return PasswordViewHolder(binding)
        }
    }
}

class PasswordDiffCallback : DiffUtil.ItemCallback<PasswordSearchResultItem>() {
    override fun areItemsTheSame(
        oldItem: PasswordSearchResultItem,
        newItem: PasswordSearchResultItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PasswordSearchResultItem,
        newItem: PasswordSearchResultItem
    ): Boolean {
        return oldItem == newItem
    }
}

typealias OnSearchItemClickListener = (View) -> Unit