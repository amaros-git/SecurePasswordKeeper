package lv.maros.secured.password.keeper.pages.passwords

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lv.maros.secured.password.keeper.databinding.PasswordItemBinding
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.models.PasswordSearchResult
import lv.maros.secured.password.keeper.views.OnCopyClickListener
import lv.maros.secured.password.keeper.views.OnPasswordClickListener

class PasswordListAdapter(
    private val passwordClickListener: OnPasswordClickListener,
    private val copyClickListener: OnCopyClickListener
) : ListAdapter<Password, PasswordViewHolder>(PasswordDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    //TODO these both variables MUST be protected against multithreads !!!!!!!!!!!!!!!!!!
    private val cachedItems = mutableListOf<Password>()
    private lateinit var mCurrentList: MutableList<Password>
    private var isSearchResultsFilterActive = false

    private fun setClickListeners(binding: PasswordItemBinding, position: Int) {
        binding.passwordItemPasswordText.setOnPasswordClickListener(passwordClickListener)

        binding.passwordItemWebsiteCopyButton.setOnCopyClickListener(position, copyClickListener)
        binding.passwordItemUsernameCopyButton.setOnCopyClickListener(position, copyClickListener)
        binding.passwordItemPasswordCopyButton.setOnCopyClickListener(position, copyClickListener)
    }

    private fun removeDuplicateIds(items: List<PasswordSearchResult>): Set<Int> {
        val ids = mutableSetOf<Int>()
        for (i in items.indices) {
            ids.add(items[i].id)
        }

        return ids
    }

    private fun cacheItems(list: List<Password>) {
        cachedItems.clear()
        list.forEach {
            cachedItems.add(it)
        }
    }

    internal fun submitMyList(list: MutableList<Password>) {
        cacheItems(list)
        mCurrentList = list

        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(mCurrentList)
            }
        }
    }

    //TODO improve filter algo !!!!!!!!!!!!!!!!!!
    @SuppressLint("NotifyDataSetChanged")
    internal fun showSearchResultItems(searchItems: List<PasswordSearchResult>) {
        mCurrentList.clear()
        for (searchItem in searchItems) {
            for (cachedItem in cachedItems) {
                if (searchItem.id == cachedItem.id) {
                    mCurrentList.add(cachedItem)
                }
            }
        }
        notifyDataSetChanged()

        isSearchResultsFilterActive = true
    }

    @SuppressLint("NotifyDataSetChanged")
    internal fun clearAllFilters() {
        mCurrentList.clear()
        cachedItems.forEach {
            mCurrentList.add(it)
        }
        notifyDataSetChanged()

        isSearchResultsFilterActive = false
    }

    internal fun isSearchResultsFilterActive() = isSearchResultsFilterActive

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
