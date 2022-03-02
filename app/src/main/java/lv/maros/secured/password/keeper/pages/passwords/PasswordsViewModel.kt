package lv.maros.secured.password.keeper.pages.passwords

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.base.BaseViewModel
import lv.maros.secured.password.keeper.data.PasswordDataSource
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.security.KeeperCryptor
import lv.maros.secured.password.keeper.utils.toPassword
import lv.maros.secured.password.keeper.utils.toPasswordDTO

class PasswordsViewModel(
    private val repository: PasswordDataSource,
    private val cryptor: KeeperCryptor,
    private val app: Application
) : BaseViewModel(app) {

    val showNoData: MutableLiveData<Boolean> = MutableLiveData()

    private val _passwordList = MutableLiveData<MutableList<Password>>()
    val passwordList: LiveData<MutableList<Password>>
        get() = _passwordList


    private suspend fun _loadAllPasswords() {
        val passwords = repository.getAllPasswords()

        if (passwords.isNotEmpty()) {
            _passwordList.value =
                passwords.map { it.toPassword() }.sortedBy { it.website }.toMutableList()
        }

        invalidateShowNoData()
    }


    fun loadAllPasswords() {
        viewModelScope.launch {
            _loadAllPasswords()
        }
    }

    /**
     * Inform the user that there's not any data if the remindersList is empty
     */
    private fun invalidateShowNoData() {
        showNoData.value = _passwordList.value == null || _passwordList.value!!.isEmpty()
    }

    fun decryptString(data: String): String {
        val decryptedData = cryptor.decryptString(data)
        return if (null != decryptedData) {
            decryptedData
        } else {
            showErrorMessage.value = (app.getString(R.string.internal_error))
            ""
        }
    }

    fun deletePassword(passwordId: Int, position: Int) {
        viewModelScope.launch {
            repository.deletePassword(passwordId)
            _passwordList.value?.removeAt(position)
            invalidateShowNoData()
        }
    }

    /*fun undoPasswordRemoval(password: Password) {
        viewModelScope.launch {
            repository.savePassword(password.toPasswordDTO())

            _loadAllPasswords()
        }
    }*/

    fun removePasswordItem(passwordListAdapter: PasswordListAdapter, swipedPos: Int) {
        _passwordList.value?.removeAt(swipedPos)
        passwordListAdapter.notifyItemRemoved(swipedPos)
    }

    fun addPasswordItem(
        passwordListAdapter: PasswordListAdapter,
        password: Password,
        swipedPos: Int
    ) {
        _passwordList.value?.add(swipedPos, password)
        passwordListAdapter.notifyItemInserted(swipedPos)
    }
}

@Suppress("UNCHECKED_CAST")
class PasswordsViewModelFactory(
    private val repository: PasswordDataSource,
    private val cryptor: KeeperCryptor,
    private val app: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (PasswordsViewModel(repository, cryptor, app) as T)

}