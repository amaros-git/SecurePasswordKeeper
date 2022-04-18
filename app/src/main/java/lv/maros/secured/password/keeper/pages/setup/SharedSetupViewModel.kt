package lv.maros.secured.password.keeper.pages.setup

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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

class SharedSetupViewModel (
    private val configStorage: KeeperConfigStorage,
    private val cryptor: KeeperCryptor,
    private val app: Application
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
                showToastEvent.value = app.getString(R.string.password_too_short)
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

@Suppress("UNCHECKED_CAST")
class SharedSetupViewModelFactory(
    private val configStorage: KeeperConfigStorage,
    private val cryptor: KeeperCryptor,
    private val app: Application
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (SharedSetupViewModel(configStorage, cryptor, app) as T)

}
