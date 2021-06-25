package lv.maros.securedpasswordkeeper.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.launch
import lv.maros.securedpasswordkeeper.security.Crypto
import timber.log.Timber
import javax.inject.Inject

class SharedSetupViewModel @Inject constructor(
    private val app: Application
) : AndroidViewModel(app) {

    @Inject
    lateinit var crypto: Crypto


    fun encryptAndSavePasskey(passkey: String) {
        viewModelScope.launch {
            crypto.encryptAndSavePasskey(passkey)
        }
    }



}