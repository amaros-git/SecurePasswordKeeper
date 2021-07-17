package lv.maros.securedpasswordkeeper.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lv.maros.securedpasswordkeeper.security.KeeperCryptor
import javax.inject.Inject

class SharedSetupViewModel @Inject constructor(
    private val app: Application
) : AndroidViewModel(app) {

    @Inject
    lateinit var crypto: KeeperCryptor


    fun encryptAndSavePasskey(passkey: String) {
        viewModelScope.launch {
            crypto.encryptAndSavePasskey(passkey)
        }
    }



}