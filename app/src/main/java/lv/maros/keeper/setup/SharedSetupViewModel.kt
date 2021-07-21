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
import lv.maros.keeper.utils.NavigationCommand
import lv.maros.keeper.utils.SingleLiveEvent
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SharedSetupViewModel @Inject constructor(
    private val configStorage: KeeperConfigStorage,
    private val app: Application
) : ViewModel() {

    val showToast: SingleLiveEvent<String> = SingleLiveEvent()

    val navigationCommand: SingleLiveEvent<NavigationCommand> = SingleLiveEvent()

    val setupIsFinished: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun savePasskey(passkey: String) {
        Timber.d("passkey = $passkey")
        viewModelScope.launch(Dispatchers.IO) {
            val hash = KeeperCryptor.hashData(passkey)
            if (null != hash) {
                val result = configStorage.saveKeeperConfigParam(
                    KeeperConfigStorage.KEEPER_CONFIG_PASSKEY_HASH,
                    hash
                )
                if (!result) {
                    //TODO
                } else {
                    setupIsFinished.postValue(true)
                }
            } else {
                showToast.postValue(app.getString(R.string.internal_error))
            }
        }
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
}