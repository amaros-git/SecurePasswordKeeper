package lv.maros.keeper

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lv.maros.keeper.utils.KeeperResult
import lv.maros.keeper.models.Password
import lv.maros.keeper.security.KeeperConfigStorage
import lv.maros.keeper.security.KeeperCryptor
import lv.maros.keeper.utils.SingleLiveEvent
import timber.log.Timber
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject


class SharedKeeperViewModel @Inject constructor(
    private val app: Application,
) : AndroidViewModel(app) {

    private val crypto = KeeperCryptor()

    @Inject
    lateinit var configStorage: KeeperConfigStorage

    val authenticationResult = SingleLiveEvent<KeeperResult>()

    fun savePassword(password: Password) {
        viewModelScope.launch(Dispatchers.Default) {

        }
    }

    fun verifyPasskey(passkey: String) {
        val hashSaved =
            configStorage.getKeeperConfigParam(KeeperConfigStorage.KEEPER_CONFIG_PASSKEY_HASH)

        if (!hashSaved.isNullOrEmpty()) {
            crypto.verifyPasskey(passkey, hashSaved)
        }
    }


}