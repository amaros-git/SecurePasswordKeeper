package lv.maros.keeper

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel


class SharedKeeperViewModel (
    private val app: Application,
) : AndroidViewModel(app) {

    fun verifyPasskey(passkey: String): Boolean {
        return false
    }

}