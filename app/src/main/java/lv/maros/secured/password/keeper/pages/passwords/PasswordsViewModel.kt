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

class PasswordsViewModel(
    private val repository: PasswordDataSource,
    private val cryptor: KeeperCryptor,
    private val app: Application
) : BaseViewModel(app) {

    val showNoData: MutableLiveData<Boolean> = MutableLiveData()

    private val _passwordList = MutableLiveData<MutableList<Password>>()
    val passwordList: LiveData<MutableList<Password>>
        get() = _passwordList


    fun loadAllPasswords() {
        viewModelScope.launch {
            val passwords = repository.getAllPasswords()

            if (passwords.isNotEmpty()) {
                _passwordList.value = passwords.map { it.toPassword() }.toMutableList()
            }

            invalidateShowNoData()
        }
    }

    /**
     * Inform the user that there's not any data if the remindersList is empty
     */
    private fun invalidateShowNoData() {
        showNoData.value = passwordList.value == null || passwordList.value!!.isEmpty()
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
        }
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