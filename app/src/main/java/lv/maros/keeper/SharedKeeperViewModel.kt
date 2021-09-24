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


    fun verifyPasskey(passkey: String): Boolean {
        return false
    }

    fun isKeeperConfigured(): Boolean {
        return configStorage.isKeeperConfigured()
    }

    fun isLoginEnabled() = configStorage.isLoginEnabled()

    fun savePassword(passwordData: PasswordInputData) {
        viewModelScope.launch(Dispatchers.IO) {
            val encryptedPassword = encryptString(passwordData.password)

            Timber.d("Encrypted password = $encryptedPassword")

            if (null != encryptedPassword) {
                val passwordDto = PasswordDTO(
                    passwordData.website,
                    passwordData.username,
                    encryptedPassword,
                    System.currentTimeMillis(),
                    0
                )
                passwordDb.passwordDao.insertPassword(passwordDto)

                Timber.d("Saved password dto: $passwordDto")
            }
            else {
                showErrorEvent.postValue(app.getString(R.string.internal_error))
            }
        }
    }

    /**
     * return empty string if Encryption Key doesn't exist
     */
    private fun encryptString(plainText: String): String? {
        val key = configStorage.getEncryptionKey()
        val iv = configStorage.getEncryptionIV()
        //Timber.d("encryption key = $encryptionKey")

        return if (null != key && null != iv) {
            cryptor.encryptString(plainText, key, iv)
        } else {
            null
        }
    }

    fun loadPassword() {
        viewModelScope.launch(Dispatchers.Main) {
            val savedPasswords = passwordDb.passwordDao.getAllPasswords()

            if (savedPasswords.isNotEmpty()) {
                val passwordToShow = ArrayList<Password>()
                passwordToShow.addAll(savedPasswords.map { passwordDto ->
                    passwordDtoToPassword(passwordDto)
                })

                _passwordList.value = passwordToShow

                Timber.d("Read from the db:")
                passwordToShow.forEach {
                    Timber.d("$it")
                }

                val encKey = configStorage.getEncryptionKey()
                encKey?.let {
                    //val result = cryptor.decryptString(passwordToShow[0].encryptedPassword, it)
                    //Timber.d("decypted password = $result")
                }

            }

            //check if no data image has to be shown
            invalidateShowNoData()
        }
    }

    private fun passwordDtoToPassword(passwordDto: PasswordDTO) =
        Password(
            passwordDto.website,
            passwordDto.username,
            passwordDto.encryptedPassword,
            passwordDto.passwordLastModificationDate,
            passwordDto.id
        )

    /**
     * Inform the user that there's not any data if the remindersList is empty
     */
    private fun invalidateShowNoData() {
        showNoData.value = passwordList.value == null || passwordList.value!!.isEmpty()
    }













    private fun TEST_saveSomePasswords() {
        val passwordList = listOf(
            PasswordDTO("www.delfi.lv", "test", "user", System.currentTimeMillis(),0),
            PasswordDTO("Amazone", "test", "user", System.currentTimeMillis(),0),
            PasswordDTO("220.lv", "test", "user", System.currentTimeMillis(),0),
            PasswordDTO("Aliexpress", "test", "user", System.currentTimeMillis(),0),
        )

        viewModelScope.launch() {
            withContext(Dispatchers.IO) {
                for (password in passwordList) {
                    passwordDb.passwordDao.insertPassword(password)
                }
            }
           /* val savedPasswords = passwordDb.passwordDao.getAllPasswords()

            val passwords = savedPasswords.map {
                Password(it.website, it.username, it.encryptedPassword, it.passwordLastModificationDate, it.id)
            }
            _passwordList.postValue(passwords)*/
        }
    }

}