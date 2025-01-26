package lv.maros.secured.password.keeper.pages.passwords.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lv.maros.secured.password.keeper.databinding.PasswordSearchItemBinding
import lv.maros.secured.password.keeper.models.PasswordSearchResult
import lv.maros.secured.password.keeper.views.OnPasswordSearchClickListener

class PasswordsSearchAdapter(
    private val clickListener: OnPasswordSearchClickListener
) : ListAdapter<PasswordSearchResult, PasswordViewHolder>(PasswordDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PasswordViewHolder.from(parent)

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        val passwordItem = getItem(position)
        holder.bind(passwordItem)

        holder.binding.passwordSearchItemView.setOnSearchItemClickListener(position, clickListener)
    }

  public override fun getItem(position: Int): PasswordSearchResult {
      return super.getItem(position)
  }

    fun submitMyList(list: List<PasswordSearchResult>) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }
}

class PasswordViewHolder(val binding: PasswordSearchItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PasswordSearchResult) {
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

class PasswordDiffCallback : DiffUtil.ItemCallback<PasswordSearchResult>() {
    override fun areItemsTheSame(
        old: PasswordSearchResult,
        aNew: PasswordSearchResult
    ): Boolean {
        return old.id == aNew.id
    }

    override fun areContentsTheSame(
        old: PasswordSearchResult,
        aNew: PasswordSearchResult
    ): Boolean {
        return old == aNew
    }
}
