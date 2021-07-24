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

class ElectionListAdapter() :
    ListAdapter<ElectionDataItem, RecyclerView.ViewHolder>(ElectionDiffCallback())
{

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PasswordViewHolder.from(parent)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val electionItem = getItem(position) as ElectionDataItem.ElectionItem
        holder.bind(electionItem.election)

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ElectionDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is ElectionDataItem.ElectionItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun submitMyList(list: List<Election>?, headerText: String? = null) {
        list?.let {
            adapterScope.launch {
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
            }
        }
    }

}

class PasswordViewHolder(
        private val binding: PasswordItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewMode: ElectionsViewModel, item: Election) {
        binding.election = item

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

class PasswordDiffCallback : DiffUtil.ItemCallback<PasswordDataItem>() {
    override fun areItemsTheSame(
            oldItem: PasswordDataItem,
            newItem: PasswordDataItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
            oldItem: PasswordDataItem,
            newItem: PasswordDataItem
    ): Boolean {
        return oldItem == newItem
    }
}