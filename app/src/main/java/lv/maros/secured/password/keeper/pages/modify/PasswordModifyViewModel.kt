package lv.maros.secured.password.keeper.pages.modify

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.base.BaseViewModel
import lv.maros.secured.password.keeper.data.local.PasswordDatabase
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.models.PasswordInputData
import lv.maros.secured.password.keeper.security.KeeperConfigStorage
import lv.maros.secured.password.keeper.security.KeeperCryptor
import lv.maros.secured.password.keeper.security.KeeperPasswordManager
import lv.maros.secured.password.keeper.utils.KeeperResult
import lv.maros.secured.password.keeper.utils.SingleLiveEvent
import lv.maros.secured.password.keeper.utils.toPassword
import lv.maros.secured.password.keeper.utils.toPasswordDTO
import timber.log.Timber
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
            val encryptedPassword = encryptString(passwordData.password)
            if (null != encryptedPassword) {
                passwordDb.passwordDao.savePassword(
                    passwordData.toPasswordDTO(encryptedPassword)
                )
            } else {
                Timber.e("Encryption result is null")
                showSnackBarInt.value = R.string.internal_error
            }
        }
    }

    fun updatePassword(passwordData: PasswordInputData) {
        val encryptedPassword = encryptString(passwordData.password)

        if (null != encryptedPassword &&
            verifyPasswordInputData(passwordData) &&
            isPasswordModified(passwordData, encryptedPassword)
        ) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    passwordDb.passwordDao.updatePassword(
                        passwordData.toPasswordDTO(
                            encryptedPassword
                        )
                    )
                }
            }
        } else {
            //TODO
        }
    }

    //TODO move to ViewModel. Rework, compare passwords etc
    private fun verifyPasswordInputData(passwordData: PasswordInputData): Boolean {
        val (website, username, password, repeatPassword) = passwordData

        return password == repeatPassword &&
                verifyPassword(password) &&
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

    private fun encryptString(plainText: String): String? {
        val key = configStorage.getEncryptionKey()
        val iv = configStorage.getEncryptionIV()

        return if (null != key && null != iv) {
            cryptor.encryptString(plainText, key, iv)
        } else {
            Timber.e("Critical error: missing encryption key or iv")
            null
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

    private fun isPasswordModified(passwordData: PasswordInputData, encryptedPassword: String): Boolean {
        val passwordToUpdate = _password.value

        return if ((null != passwordToUpdate)) {
            (passwordData.website != passwordToUpdate.website) ||
            (passwordData.username != passwordToUpdate.username) ||
            (encryptedPassword != passwordToUpdate.encryptedPassword)
        } else {
            false
        }

    }

}