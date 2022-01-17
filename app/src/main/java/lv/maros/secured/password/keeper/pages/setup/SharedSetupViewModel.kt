package lv.maros.secured.password.keeper.pages.setup

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.models.KeeperConfig
import lv.maros.secured.password.keeper.security.KeeperConfigStorage
import lv.maros.secured.password.keeper.security.KeeperCryptor
import lv.maros.secured.password.keeper.security.KeeperPasswordManager
import lv.maros.secured.password.keeper.utils.KEEPER_AUTH_TYPE_NONE
import lv.maros.secured.password.keeper.utils.KEEPER_PASSKEY_PIN_MIN_LENGTH
import lv.maros.secured.password.keeper.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class SharedSetupViewModel @Inject constructor(
    private val app: Application,
    private val configStorage: KeeperConfigStorage,
    private val cryptor: KeeperCryptor
) : ViewModel() {


    val showToastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    val setupIsFinishedEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()

    val authenticationIsConfiguredEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()

    //TODO rework and use error field on TextViewLayout
    fun verifyPasskeys(passkey1: String, passkey2: String): Boolean {
        return when {
            passkey1.isEmpty() || passkey2.isEmpty() -> {
                showToastEvent.value = app.getString(R.string.password_blank_or_empty_error)
                false
            }
            passkey1.length < KEEPER_PASSKEY_PIN_MIN_LENGTH ||
                    passkey2.length < KEEPER_PASSKEY_PIN_MIN_LENGTH -> {
                showToastEvent.value = app.getString(R.string.password_min_len_error)
                false
            }
            passkey1 != passkey2 -> {
                showToastEvent.value = app.getString(R.string.password_do_not_match_error)
                false
            }
            else -> passkey1 == passkey2
        }
    }

    fun finishSetup() {
        //verify config
        setupIsFinishedEvent.value = true
    }

    //TODO re-factor
    fun initKeeperConfig() {
        viewModelScope.launch(Dispatchers.IO) {
            val encryptionKey = KeeperPasswordManager.generateEncryptionKey()
            val iv = KeeperPasswordManager.generateEncryptionIV()

            configStorage.saveOrUpdateKeeperConfig(
                KeeperConfig(
                KEEPER_AUTH_TYPE_NONE,
                null,
                encryptionKey,
                iv,
                false
            )
            )
        }
    }

    fun completeAuthConfigurationAndNavigate(passkey: String, authType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newConfig = configStorage.getKeeperConfig().apply {
                this.passkeyHash = passkey
                this.authType = authType
            }

            if (configStorage.saveOrUpdateKeeperConfig(newConfig)) {
                authenticationIsConfiguredEvent.postValue(true)
            }
            else {
                showToastEvent.postValue(app.getString(R.string.internal_error))
            }
        }
    }
}