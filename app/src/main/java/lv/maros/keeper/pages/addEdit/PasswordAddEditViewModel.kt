package lv.maros.keeper.pages.addEdit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lv.maros.keeper.R
import kotlinx.coroutines.runBlocking
import lv.maros.keeper.data.local.PasswordDatabase
import lv.maros.keeper.data.dto.PasswordDTO
import lv.maros.keeper.models.Password
import lv.maros.keeper.models.PasswordInputData
import lv.maros.keeper.security.KeeperConfigStorage
import lv.maros.keeper.security.KeeperCryptor
import lv.maros.keeper.utils.KeeperResult
import lv.maros.keeper.utils.SingleLiveEvent
import lv.maros.keeper.utils.toPassword
import lv.maros.keeper.utils.toPasswordDTO
import javax.inject.Inject

@HiltViewModel
class PasswordAddEditViewModel @Inject constructor(
    private val configStorage: KeeperConfigStorage,
    private val passwordDb: PasswordDatabase,
    private val cryptor: KeeperCryptor,
    private val app: Application
) : AndroidViewModel(app) {

    val showErrorEvent = SingleLiveEvent<String>()

    private val _password = MutableLiveData<Password>()
    val password: LiveData<Password>
        get() = _password

    fun verifyPasskey(passkey: String): Boolean {
        return false
    }

    fun savePassword(passwordData: PasswordInputData) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val encryptionResult = encryptString(passwordData.password)) {
                is KeeperResult.Success -> {
                    passwordDb.passwordDao.insertPassword(
                        passwordData.toPasswordDTO(encryptionResult.data)
                    )
                }

                is KeeperResult.Error -> {
                    showErrorEvent.postValue(app.getString(R.string.internal_error))
                }
            }
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

}