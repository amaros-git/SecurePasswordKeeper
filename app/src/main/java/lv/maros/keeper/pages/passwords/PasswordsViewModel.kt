package lv.maros.keeper.pages.passwords

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import lv.maros.keeper.R
import lv.maros.keeper.data.local.PasswordDatabase
import lv.maros.keeper.data.dto.PasswordDTO
import lv.maros.keeper.models.Password
import lv.maros.keeper.models.PasswordInputData
import lv.maros.keeper.security.KeeperConfigStorage
import lv.maros.keeper.security.KeeperCryptor
import lv.maros.keeper.utils.KeeperResult
import lv.maros.keeper.utils.SingleLiveEvent
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PasswordsViewModel @Inject constructor(
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


    init {
        TEST_saveSomePasswords()
    }

    fun ByteArray.toHexString(): String {
        val stringBuf = StringBuffer()
        this.forEach {
            stringBuf.append(0xFF and it.toInt())
            stringBuf.append(" ")
        }

        return stringBuf.toString()
    }

    fun printByteArrayInOneLine(bytes: ByteArray, separator: String) {
        val stringBuf = StringBuffer()
        bytes.forEach {
            stringBuf.append(it.toInt())
            stringBuf.append(separator)
        }

        Timber.d(stringBuf.toString())

    }

    fun verifyPasskey(passkey: String): Boolean {
        return false
    }

    fun isKeeperConfigured(): Boolean {
        return configStorage.isKeeperConfigured()
    }

    fun isLoginEnabled() = configStorage.isLoginEnabled()

    fun saveAndNavigateIfSuccess(passwordData: PasswordInputData) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val encryptionResult = encryptString(passwordData.password)) {
                is KeeperResult.Success -> {
                    val passwordDto = PasswordDTO(
                        passwordData.website,
                        passwordData.username,
                        encryptionResult.data,
                        System.currentTimeMillis(),
                        0
                    )
                    passwordDb.passwordDao.insertPassword(passwordDto)
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

    fun loadPassword() {
        viewModelScope.launch(Dispatchers.Main) {
            val savedPasswords = passwordDb.passwordDao.getAllPasswords()

            if (savedPasswords.isNotEmpty()) {
                val passwordToShow = ArrayList<Password>()
                passwordToShow.addAll(savedPasswords.map { passwordDto ->
                    passwordDtoToPassword(passwordDto)
                })

                _passwordList.value = passwordToShow
            }

            //check if no data image has to be shown
            invalidateShowNoData()
        }
    }

    fun getPassword(passwordId: Int): Password? {
        return runBlocking {
            val passwordDto = passwordDb.passwordDao.getPassword(passwordId)
            if (null != passwordDto) {
                passwordDtoToPassword(passwordDto)
            } else {
                null
            }
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
            PasswordDTO("www.delfi.lv", "test", "user", System.currentTimeMillis(), 0),
            PasswordDTO("Amazone", "test", "user", System.currentTimeMillis(), 0),
            PasswordDTO("220.lv", "test", "user", System.currentTimeMillis(), 0),
            PasswordDTO("Aliexpress", "test", "user", System.currentTimeMillis(), 0),
        )

        viewModelScope.launch {
            if (passwordDb.passwordDao.getAllPasswords().isEmpty()) {
                withContext(Dispatchers.IO) {
                    for (password in passwordList) {
                        passwordDb.passwordDao.insertPassword(password)
                    }
                }
            }
        }
    }

}