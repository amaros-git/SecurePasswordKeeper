package lv.maros.keeper.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lv.maros.keeper.security.KeeperCryptor
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SharedSetupViewModel @Inject constructor(
    private val app: Application
) : AndroidViewModel(app) {

    @Inject lateinit var cryptor: KeeperCryptor

    fun encryptAndSavePasskey(passkey: String) {
        Timber.d("passkey = $passkey")
        viewModelScope.launch {
            cryptor.hashPasskey(passkey)
        }
    }


    
}