package lv.maros.keeper

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lv.maros.keeper.authentication.KeeperAuthenticator
import javax.inject.Inject

@HiltViewModel
class SharedKeeperViewModel @Inject constructor(
    private val authenticator: KeeperAuthenticator,
    private val app: Application,
) : AndroidViewModel(app) {

    fun verifyPasskey(passkey: String): Boolean {
        return false
    }

    fun isKeeperConfigured(): Boolean {
        return authenticator.isKeeperConfigured()
    }

}