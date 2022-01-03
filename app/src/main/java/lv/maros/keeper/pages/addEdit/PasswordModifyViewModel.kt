package lv.maros.keeper.pages.addEdit

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lv.maros.keeper.R
import lv.maros.keeper.base.BaseViewModel
import lv.maros.keeper.data.local.PasswordDatabase
import lv.maros.keeper.models.Password
import lv.maros.keeper.models.PasswordInputData
import lv.maros.keeper.security.KeeperConfigStorage
import lv.maros.keeper.security.KeeperCryptor
import lv.maros.keeper.security.KeeperPasswordManager
import lv.maros.keeper.utils.KeeperResult
import lv.maros.keeper.utils.SingleLiveEvent
import lv.maros.keeper.utils.toPassword
import lv.maros.keeper.utils.toPasswordDTO
import javax.inject.Inject

@HiltViewModel
class PasswordModifyViewModel @Inject constructor(
    private val configStorage: KeeperConfigStorage,
    private val passwordDb: PasswordDatabase,
    private val cryptor: KeeperCryptor,
    private val app: Application
) : BaseViewModel(app) {


    private val _password = MutableLiveData<Password>()
    val password: LiveData<Password>
        get() = _password

    val websiteError: SingleLiveEvent<String> = SingleLiveEvent()
    val usernameError: SingleLiveEvent<String> = SingleLiveEvent()
    val passwordError: SingleLiveEvent<String> = SingleLiveEvent()
    val repeatPasswordError: SingleLiveEvent<String> = SingleLiveEvent()


    fun savePassword(passwordData: PasswordInputData) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val encryptionResult = encryptString(passwordData.password)) {
                is KeeperResult.Success -> {
                    passwordDb.passwordDao.insertPassword(
                        passwordData.toPasswordDTO(encryptionResult.data)
                    )
                }

                is KeeperResult.Error -> {
                    showSnackBarInt.value = R.string.internal_error
                }
            }
        }
    }

    fun updatePassword(passwordData: PasswordInputData) {
        if (!verifyPasswordInputData(passwordData)) {
            //TODO
        }
            //isPasswordModified()


    }

    //TODO move to ViewModel. Rework, compare passwords etc
    private fun verifyPasswordInputData(passwordData: PasswordInputData): Boolean {
        val (website, username, password, repeatPassword) = passwordData

        return  verifyPassword(password) &&
                verifyPassword(repeatPassword)


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

    private fun encryptString(plainText: String): KeeperResult<String> {
        val key = configStorage.getEncryptionKey()
        val iv = configStorage.getEncryptionIV()
        //Timber.d("encryption key = $encryptionKey")

        return if (null != key && null != iv) {
            cryptor.encryptString(plainText, key, iv)
        } else {
            KeeperResult.Error("Encryption key or iv doesn't exist")
        }
    }

    fun loadPassword(passwordId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val passwordDTO = passwordDb.passwordDao.getPassword(passwordId)

            if (null != passwordDTO) {
                _password.postValue(passwordDTO.toPassword())
            } else {
                //TODO
            }
        }
    }

    fun isPasswordModified(passwordData: PasswordInputData, password: Password): Boolean {
        val encryptedPassword = passwordData.password

        return (passwordData.website != password.website) ||
                (passwordData.username != password.username) ||
                (encryptedPassword != password.encryptedPassword)

    }

}