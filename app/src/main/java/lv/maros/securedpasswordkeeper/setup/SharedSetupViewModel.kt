package lv.maros.securedpasswordkeeper.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import timber.log.Timber
import javax.inject.Inject

class SharedSetupViewModel @Inject constructor(
    private val app: Application
) : AndroidViewModel(app) {

    fun savePasskey(passkey: String) {
        Timber.d("savePasskey called")
    }

}