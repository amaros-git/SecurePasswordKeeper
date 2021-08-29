package lv.maros.keeper.setup

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lv.maros.keeper.R
import lv.maros.keeper.models.KeeperConfig
import lv.maros.keeper.security.KeeperConfigStorage
import lv.maros.keeper.security.KeeperCryptor
import lv.maros.keeper.security.KeeperPasswordGenerator
import lv.maros.keeper.utils.PASSKEY_MIN_LENGTH
import lv.maros.keeper.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class SharedSetupViewModel @Inject constructor(
    private val app: Application,
    private val configStorage: KeeperConfigStorage,
    private val cryptor: KeeperCryptor
) : ViewModel() {


    val showToast: SingleLiveEvent<String> = SingleLiveEvent()

    val setupIsFinishedEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()

    val authenticationIsConfiguredEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()


    fun verifyPasskeys(passkey1: String, passkey2: String): Boolean {
        return if (passkey1.isEmpty() || passkey2.isEmpty()) {
            showToast.value = app.getString(R.string.passkey_empty_error)
            false
        } else if ((passkey1.length < PASSKEY_MIN_LENGTH) ||
            (passkey2.length < PASSKEY_MIN_LENGTH)
        ) {
            showToast.value = app.getString(R.string.passkey_min_len_error)
            false
        } else if (passkey1 != passkey2) {
            showToast.value = app.getString(R.string.passkey_dont_match_error)
            false
        } else passkey1 == passkey2
    }

    fun finishSetup() {
        //verify config
        setupIsFinishedEvent.value = true
    }


    fun updateKeeperConfig(newConfig: KeeperConfig) {
        viewModelScope.launch(Dispatchers.IO) {
            if(!configStorage.saveKeeperConfig(newConfig)) {
                //TODO show error
            }
        }
    }

    fun savePasskeyAndAuthType(passkey: String, authType: String) {
        val currentConfig = configStorage.getKeeperConfig()

        updateKeeperConfig(KeeperConfig(
            authType,
            passkey,
            currentConfig.encryptionKey,
            currentConfig.encryptionIV,
            currentConfig.useLogin
        ))
    }

    fun createEncryptionKey() = KeeperPasswordGenerator().generatePassword()

    fun createEncryptionIV() = KeeperPasswordGenerator().generateIV()
}