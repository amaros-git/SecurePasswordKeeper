package lv.maros.keeper.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lv.maros.keeper.R
import lv.maros.keeper.security.KeeperCryptor
import lv.maros.keeper.setup.views.PasskeyInputBottomDialog
import lv.maros.keeper.utils.KeeperResult
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SharedSetupViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var cryptor: KeeperCryptor


    fun encryptAndSavePasskey(passkey: String) {
        Timber.d("passkey = $passkey")
        viewModelScope.launch {
            cryptor.encryptAndSavePasskey(passkey)
        }
    }

    fun verifyPasskeys(passkey1: String, passkey2: String): KeeperResult {
        /*return if (passkey1.isEmpty() || passkey2.isEmpty()) {
            KeeperResult.Error(app.getString(R.string.passkey_empty_error))
        }
        else if ((passkey1.length < PasskeyInputBottomDialog.PASSKEY_MIN_LENGTH) ||
            (passkey1.length < PasskeyInputBottomDialog.PASSKEY_MIN_LENGTH)
        ) {
            KeeperResult.Error(app.getString(R.string.passkey_min_len_error))
        }
        else if (passkey1 != passkey2) {
            KeeperResult.Error(app.getString(R.string.passkey_dont_match_error))
        }
        else {
            KeeperResult.Success(passkey1)
        }*/

        return       KeeperResult.Success(passkey1)
    }


}