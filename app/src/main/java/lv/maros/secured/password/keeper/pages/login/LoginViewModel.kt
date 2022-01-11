package lv.maros.keeper.pages.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lv.maros.keeper.security.KeeperConfigStorage
import lv.maros.keeper.utils.SingleLiveEvent
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val configStorage: KeeperConfigStorage,
    app: Application
) : AndroidViewModel(app) {

    val showErrorEvent = SingleLiveEvent<String>()

    fun verifyPasskey(passkey: String): Boolean {
        return false
    }

    fun isLoginEnabled() = configStorage.isLoginEnabled()

}