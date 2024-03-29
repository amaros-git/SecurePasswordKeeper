package lv.maros.secured.password.keeper.pages.passwords.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import lv.maros.secured.password.keeper.base.BaseViewModel
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.models.PasswordSearchResult

class PasswordSearchViewModel(
    private val passwords: List<Password>,
    private val app: Application
) : BaseViewModel(app) {

    val showNoData: MutableLiveData<Boolean> = MutableLiveData()

    private val _searchResult = MutableLiveData<List<PasswordSearchResult>>()
    val searchResult: LiveData<List<PasswordSearchResult>>
        get() = _searchResult

    private fun invalidateShowNoData() {
        showNoData.value = _searchResult.value == null || _searchResult.value!!.isEmpty()
    }

    private fun getSearchSuggestions(key: CharSequence): List<PasswordSearchResult> {
        val tempList = mutableListOf<PasswordSearchResult>()
        passwords.forEach {
            if (it.website.contains(key, true)) {
                tempList.add(PasswordSearchResult(
                    it.website,
                    PasswordSearchResult.TYPE_WEBSITE,
                    it.id
                ))
            }
            if (it.username.contains(key, true)) {
                tempList.add(PasswordSearchResult(
                    it.username,
                    PasswordSearchResult.TYPE_USERNAME,
                    it.id
                ))
            }
        }

        return tempList.toList()
    }


    internal fun showSearchSuggestions(key: CharSequence) {
        _searchResult.value = getSearchSuggestions(key)
    }

    fun clearSearchSuggestions() {
        _searchResult.value = emptyList()
    }

}

    @Suppress("UNCHECKED_CAST")
    class PasswordSearchViewModelFactory(
        private val passwords: List<Password>,
        private val app: Application
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>) =
            (PasswordSearchViewModel(passwords, app) as T)

    }

