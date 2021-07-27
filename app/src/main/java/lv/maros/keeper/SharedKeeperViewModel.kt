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
import lv.maros.keeper.data.database.PasswordDatabase
import lv.maros.keeper.models.Password
import lv.maros.keeper.security.KeeperConfigStorage
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SharedKeeperViewModel @Inject constructor(
    private val configStorage: KeeperConfigStorage,
    private val passwordDb: PasswordDatabase,
    private val app: Application,
) : AndroidViewModel(app) {

    val showNoData: MutableLiveData<Boolean> = MutableLiveData()

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

    fun isLoginEnabled(): Boolean {
        return configStorage.getKeeperBooleanConfigParam(KeeperConfigStorage.KEEPER_CONFIG_USE_LOGIN_STRING)
    }


    private fun TEST_saveSomePasswords() {
        val passwords = listOf(
            Password(0, "www.delfi.lv", "test", "user", "sfsdfsdf", System.currentTimeMillis()),
            Password(0, "www.delfi.lv", "test", "user", "folkmghf", System.currentTimeMillis()),
            Password(0, "www.delfi.lv", "test", "user", "sfsdfsdf", System.currentTimeMillis()),
            Password(0, "www.delfi.lv", "test", "user", "4534terfs", System.currentTimeMillis()),
            Password(0, "www.delfi.lv", "test", "user", "rrghy55", System.currentTimeMillis())
        )

        viewModelScope.launch() {
            withContext(Dispatchers.IO) {
                for (password in passwords) {
                    passwordDb.passwordDao.insertPassword(password)
                }
            }

            val savedPasswords = passwordDb.passwordDao.getPasswords()
/*
            savedPasswords.forEach {
                Timber.d(it.toString())
            }
*/

            _passwordList.postValue(savedPasswords)
        }
    }

}