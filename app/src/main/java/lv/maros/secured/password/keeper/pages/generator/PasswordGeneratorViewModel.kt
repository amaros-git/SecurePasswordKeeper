package lv.maros.secured.password.keeper.pages.generator

import android.app.Application
import lv.maros.secured.password.keeper.base.BaseViewModel
import timber.log.Timber

class PasswordGeneratorViewModel(private val app: Application) : BaseViewModel(app) {

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared called")
    }
}
