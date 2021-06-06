package lv.maros.securedpasswordkeeper.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lv.maros.securedpasswordkeeper.Password
import lv.maros.securedpasswordkeeper.SharedPasswordViewModel
import lv.maros.securedpasswordkeeper.databinding.HeaderItemBinding
import lv.maros.securedpasswordkeeper.databinding.PasswordItemBinding


private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class PasswordListAdapter(
        private val viewModel: SharedPasswordViewModel
) : ListAdapter<PasswordDataItem, RecyclerView.ViewHolder>(PasswordDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> Header.from(parent)
            ITEM_VIEW_TYPE_ITEM -> PasswordViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PasswordViewHolder -> {
                val electionItem = getItem(position) as PasswordDataItem.PasswordItem
                holder.bind(viewModel, electionItem.password)
            }
            is Header -> {
                val headerItem = getItem(position) as PasswordDataItem.HeaderItem
                holder.bind(headerItem.headerText)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PasswordDataItem.HeaderItem -> ITEM_VIEW_TYPE_HEADER
            is PasswordDataItem.PasswordItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun submitMyList(list: List<Password>?, headerText: String? = null) {
        list?.let {
            adapterScope.launch {
                val items = if (null != headerText) {
                    listOf(PasswordDataItem.HeaderItem(headerText)) + list.map {
                        PasswordDataItem.PasswordItem(it)
                    }
                } else {
                    list.map { PasswordDataItem.PasswordItem(it) }
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

    fun bind(viewMode: SharedPasswordViewModel, item: Password) {
        binding.viewModel = viewMode

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

class Header private constructor(
        private val binding: HeaderItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(text: String) {
        binding.headerText = text

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): Header {
            val binding = HeaderItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
            )
            return Header(binding)
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

sealed class PasswordDataItem {
    data class PasswordItem(val password: Password) : PasswordDataItem() {
        override val id = password.id.toLong()
    }

    data class HeaderItem(val headerText: String) : PasswordDataItem() {
        override val id = Long.MAX_VALUE
    }

    abstract val id: Long
}
