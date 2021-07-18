package lv.maros.keeper

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lv.maros.keeper.utils.KeeperResult
import lv.maros.keeper.models.Password
import lv.maros.keeper.security.KeeperCryptor
import lv.maros.keeper.utils.SingleLiveEvent
import timber.log.Timber
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject


class SharedKeeperViewModel @Inject constructor (
    private val app: Application,
) : AndroidViewModel(app) {

    private val crypto = KeeperCryptor()

    val authenticationResult = SingleLiveEvent<KeeperResult>()

    fun savePassword(password: Password) {
        viewModelScope.launch(Dispatchers.Default)  {

        }
    }

    fun verifyPasskey(passkey: String) {
        Timber.d("verifyPasskey called")
        viewModelScope.launch {  }
        crypto.verifyPasskey(passkey)
    }

    fun encryptPassword(passwordString: String) {


        val plaintext: ByteArray = "test".toByteArray()
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(256)

        val key: SecretKey = keygen.generateKey()
        Timber.d("key = $key")

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val ciphertext: ByteArray = cipher.doFinal(plaintext)
        val iv: ByteArray = cipher.iv

        Timber.d(iv.toString())
    }

}