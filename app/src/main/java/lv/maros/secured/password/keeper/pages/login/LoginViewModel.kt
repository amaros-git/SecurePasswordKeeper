package lv.maros.secured.password.keeper.pages.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import lv.maros.secured.password.keeper.security.KeeperConfigStorage
import lv.maros.secured.password.keeper.utils.SingleLiveEvent

class LoginViewModel (
    private val configStorage: KeeperConfigStorage,
    app: Application
) : AndroidViewModel(app) {

    val showErrorEvent = SingleLiveEvent<String>()

    fun verifyPasskey(passkey: String): Boolean {
        return false
    }

    fun isLoginEnabled() = configStorage.isLoginEnabled()

}

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(
    private val configStorage: KeeperConfigStorage,
    private val app: Application
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (LoginViewModel(configStorage, app) as T)

}