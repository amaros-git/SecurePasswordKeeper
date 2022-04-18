package lv.maros.secured.password.keeper.pages.passwords.search

import android.app.Application
import androidx.lifecycle.*
import lv.maros.secured.password.keeper.base.BaseViewModel
import lv.maros.secured.password.keeper.models.Password

class PasswordSearchViewModel(
    private val app: Application
) : BaseViewModel(app) {

    val showNoData: MutableLiveData<Boolean> = MutableLiveData()

    private val _searchResult = MutableLiveData<MutableList<PasswordSearchResultItem>>()
    val searchResult: LiveData<MutableList<PasswordSearchResultItem>>
        get() = _searchResult

    private fun invalidateShowNoData() {
        showNoData.value = _searchResult.value == null || _searchResult.value!!.isEmpty()
    }


    internal fun searchByKeyPhrase(s: CharSequence) {
        searchResult
    }
}

@Suppress("UNCHECKED_CAST")
class PasswordSearchViewModelFactory(
    private val app: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (PasswordSearchViewModel(app) as T)

}

data class PasswordSearchResultItem(
    val website: String,
    val username: String,
    val id: Int
)