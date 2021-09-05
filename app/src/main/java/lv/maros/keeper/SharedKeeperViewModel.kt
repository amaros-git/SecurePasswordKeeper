package lv.maros.keeper

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lv.maros.keeper.data.local.PasswordDatabase
import lv.maros.keeper.data.dto.PasswordDTO
import lv.maros.keeper.models.Password
import lv.maros.keeper.models.PasswordInputData
import lv.maros.keeper.security.KeeperConfigStorage
import lv.maros.keeper.security.KeeperCryptor
import lv.maros.keeper.utils.PASSWORD_MIN_LENGTH
import lv.maros.keeper.utils.SingleLiveEvent
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SharedKeeperViewModel @Inject constructor(
    private val configStorage: KeeperConfigStorage,
    private val passwordDb: PasswordDatabase,
    private val cryptor: KeeperCryptor,
    private val app: Application
) : AndroidViewModel(app) {

    val showNoData: MutableLiveData<Boolean> = MutableLiveData()

    val showErrorEvent = SingleLiveEvent<String>()

    private val _passwordList = MutableLiveData<List<Password>>()
    val passwordList: LiveData<List<Password>>
        get() = _passwordList

    init { //TODO REMOVE WHEN TEST ARE READY
        TEST_saveSomePasswords()
    }

    fun verifyPasskey(passkey: String): Boolean {
        return false
    }

    fun isKeeperConfigured(): Boolean {
        return configStorage.isKeeperConfigured()
    }

    fun isLoginEnabled() = configStorage.isLoginEnabled()

    fun savePassword(passwordData: PasswordInputData) {

    }

    /**
     * return empty string if Encryption Key doesn't exist
     */
    private fun encryptString(plainText: String): String? {
        val encryptionKey = configStorage.getEncryptionKey()
        Timber.d("encryption key = $encryptionKey")

        return if (null != encryptionKey) {
            cryptor.encryptString(plainText, encryptionKey)
        } else {
            null
        }
    }
















    private fun TEST_saveSomePasswords() {
        val passwordList = listOf(
            PasswordDTO("www.delfi.lv", "test", "user", System.currentTimeMillis(),0),
            PasswordDTO("www.delfi.lv", "test", "user", System.currentTimeMillis(),0),
            PasswordDTO("www.delfi.lv", "test", "user", System.currentTimeMillis(),0),
            PasswordDTO("www.delfi.lv", "test", "user", System.currentTimeMillis(),0),
            PasswordDTO("www.delfi.lv", "test", "user", System.currentTimeMillis(),0)
        )

        viewModelScope.launch() {
            withContext(Dispatchers.IO) {
                for (password in passwordList) {
                    passwordDb.passwordDao.insertPassword(password)
                }
            }
            val savedPasswords = passwordDb.passwordDao.getPasswords()

            val passwords = savedPasswords.map {
                Password(it.website, it.username, it.encryptedPassword, it.passwordLastModificationDate, it.id)
            }
            _passwordList.postValue(passwords)
        }
    }

}