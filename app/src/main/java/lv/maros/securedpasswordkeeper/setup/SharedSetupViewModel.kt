package lv.maros.securedpasswordkeeper.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import lv.maros.securedpasswordkeeper.security.Crypto
import timber.log.Timber
import javax.inject.Inject

class SharedSetupViewModel @Inject constructor(
    private val app: Application
) : AndroidViewModel(app) {

    private val crypto = Crypto(app)



}