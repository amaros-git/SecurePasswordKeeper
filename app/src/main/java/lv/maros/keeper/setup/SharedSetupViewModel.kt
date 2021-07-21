package lv.maros.keeper.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lv.maros.keeper.R
import lv.maros.keeper.authentication.KeeperAuthenticator
import lv.maros.keeper.security.KeeperConfigStorage
import lv.maros.keeper.security.KeeperCryptor
import lv.maros.keeper.setup.views.ConfigureAuthMethodFragment_GeneratedInjector
import lv.maros.keeper.setup.views.FinishSetupFragment
import lv.maros.keeper.utils.NavigationCommand
import lv.maros.keeper.utils.SingleLiveEvent
import timber.log.Timber
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

    fun savePasskey(passkey: String, keeperAuthType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val hash = cryptor.hashData(passkey)
            if (saveParamsToConfig(hash, keeperAuthType)) {
                authenticationIsConfiguredEvent.postValue(true)
            }
            else {
                //TODO
            }
        }
    }

    private fun saveParamsToConfig(hash: String, keeperAuthType: String): Boolean {
        return configStorage.saveConfigParams(mapOf(
            KeeperConfigStorage.KEEPER_CONFIG_AUTH_TYPE to keeperAuthType,
            KeeperConfigStorage.KEEPER_CONFIG_PASSKEY_HASH to hash
        ))
    }

    fun verifyPasskeys(passkey1: String, passkey2: String): Boolean {
        return if (passkey1.isEmpty() || passkey2.isEmpty()) {
            showToast.value = app.getString(R.string.passkey_empty_error)
            false
        }
        else if ((passkey1.length < KeeperAuthenticator.PASSKEY_MIN_LENGTH) ||
            (passkey2.length < KeeperAuthenticator.PASSKEY_MIN_LENGTH)) {
            showToast.value = app.getString(R.string.passkey_min_len_error)
            false
        }
        else if (passkey1 != passkey2) {
            showToast.value = app.getString(R.string.passkey_dont_match_error)
            false
        }
        else passkey1 == passkey2
    }

    fun finishSetup() {
        //Verify

        setupIsFinishedEvent.value = true
    }
}