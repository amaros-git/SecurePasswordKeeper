package lv.maros.secured.password.keeper.pages.modify

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.base.BaseViewModel
import lv.maros.secured.password.keeper.base.NavigationCommand
import lv.maros.secured.password.keeper.data.local.PasswordsLocalRepository
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.models.PasswordInputData
import lv.maros.secured.password.keeper.security.KeeperConfigStorage
import lv.maros.secured.password.keeper.security.KeeperCryptor
import lv.maros.secured.password.keeper.security.KeeperPasswordManager
import lv.maros.secured.password.keeper.utils.*
import javax.inject.Inject

@HiltViewModel
class PasswordModifyViewModel @Inject constructor(
    private val repository: PasswordsLocalRepository,
    private val configStorage: KeeperConfigStorage,
    private val cryptor: KeeperCryptor,
    private val app: Application
) : BaseViewModel(app) {

    //val _navigationCommand: SingleLiveEvent<NavigationCommand> = SingleLiveEvent()

    private val _password = MutableLiveData<Password>()
    val password: LiveData<Password>
        get() = _password

    val websiteError: SingleLiveEvent<String> = SingleLiveEvent()
    val usernameError: SingleLiveEvent<String> = SingleLiveEvent()
    val passwordError: SingleLiveEvent<String> = SingleLiveEvent()
    val repeatPasswordError: SingleLiveEvent<String> = SingleLiveEvent()


    fun savePassword(passwordData: PasswordInputData) {
        if (verifyPasswordInputData(passwordData)) {
            viewModelScope.launch {
                val encryptedPassword = encryptString(passwordData.password)
                repository.savePassword(passwordData.toPasswordDTO(encryptedPassword))
            }
        } else {
            //TODO
        }
    }

    fun updatePassword(passwordData: PasswordInputData) {
        if (verifyPasswordInputData(passwordData)) {
            viewModelScope.launch {
                repository.updatePassword(
                    passwordData.toPasswordDTO(
                        encryptString(passwordData.password)
                    )
                )

                navigationCommand.value = NavigationCommand.Back
            }
        } else {
            //TODO
        }
    }

    //TODO move to ViewModel. Rework, compare passwords etc
    private fun verifyPasswordInputData(passwordData: PasswordInputData): Boolean {
        val (website, username, password, repeatPassword) = passwordData

        if (password.isBlankOrEmpty()) {
            passwordError.value = app.getString(R.string.password_blank_or_empty_error)
        } else if (repeatPassword.isBlankOrEmpty()){
            repeatPasswordError.value = app.getString(R.string.password_blank_or_empty_error)
        } else if (password != repeatPassword) {
            passwordError.value = app.getString(R.string.password_do_not_match_error)
            repeatPasswordError.value = app.getString(R.string.password_do_not_match_error)
        }

        return false
    }

    //TODO if key and iv doesn't exist this is a critical issue. It doesn't make return null. REWROK !!!
    private fun encryptString(plainText: String): String {
        val key = configStorage.getEncryptionKey()
        val iv = configStorage.getEncryptionIV()

        return cryptor.encryptString(plainText, key!!, iv!!)
    }

    fun loadPassword(passwordId: Int) {
        viewModelScope.launch {
            val passwordDTO = repository.getPassword(passwordId)

            if (null != passwordDTO) {
                _password.postValue(passwordDTO.toPassword())
            } else {
                //TODO
            }
        }
    }

}