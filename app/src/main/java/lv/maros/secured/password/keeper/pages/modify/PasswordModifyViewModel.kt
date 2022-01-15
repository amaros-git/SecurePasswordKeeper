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
        if (!verifyPasswordInputData(passwordData)) {
            showErrorMessage.value = "Wrong data"
            return
        }

        viewModelScope.launch {
            repository.updatePassword(
                passwordData.toPasswordDTO(
                    encryptString(passwordData.password)
                )
            )

            navigationCommand.value = NavigationCommand.Back
        }
    }

    //TODO move to ViewModel. Rework, compare passwords etc
    private fun verifyPasswordInputData(passwordData: PasswordInputData): Boolean {
        val (website, username, password, repeatPassword) = passwordData

        return password == repeatPassword &&
                verifyPassword(password) &&
                verifyPassword(repeatPassword) &&
                website.isNotBlankOrEmpty() &&
                username.isNotBlankOrEmpty()
    }

    private fun verifyPassword(password: String): Boolean {
        val passwordResult = KeeperPasswordManager.verifyPassword(password)
        return if (passwordResult is KeeperResult.Error) {
            passwordError.value = passwordResult.value
            false
        } else {
            true
        }
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