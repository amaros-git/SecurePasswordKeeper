package lv.maros.secured.password.keeper.pages.passwords

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lv.maros.secured.password.keeper.base.BaseViewModel
import lv.maros.secured.password.keeper.data.local.PasswordsLocalRepository
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.utils.toPassword
import javax.inject.Inject

@HiltViewModel
class PasswordsViewModel @Inject constructor(
    private val repository: PasswordsLocalRepository,
    app: Application
) : BaseViewModel(app) {

    val showNoData: MutableLiveData<Boolean> = MutableLiveData()

    private val _passwordList = MutableLiveData<List<Password>>()
    val passwordList: LiveData<List<Password>>
        get() = _passwordList


    fun loadAllPasswords() {
        viewModelScope.launch {
            val passwords = repository.getAllPasswords()

            if (passwords.isNotEmpty()) {
                _passwordList.value = passwords.map { it.toPassword() }
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

}