package lv.maros.secured.password.keeper.pages.addEdit

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.base.BaseViewModel
import lv.maros.secured.password.keeper.base.NavigationCommand
import lv.maros.secured.password.keeper.data.PasswordDataSource
import lv.maros.secured.password.keeper.data.local.PasswordsLocalRepository
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.models.PasswordInputData
import lv.maros.secured.password.keeper.pages.login.LoginViewModel
import lv.maros.secured.password.keeper.security.KeeperConfigStorage
import lv.maros.secured.password.keeper.security.KeeperCryptor
import lv.maros.secured.password.keeper.utils.*

class PasswordAddEditViewModel (
    private val repository: PasswordDataSource,
    private val configStorage: KeeperConfigStorage,
    private val cryptor: KeeperCryptor,
    private val app: Application
) : BaseViewModel(app) {

    //val _navigationCommand: SingleLiveEvent<NavigationCommand> = SingleLiveEvent()

    private val _password = MutableLiveData<Password>()
    val password: LiveData<Password>
        get() = _password

    val websiteError: SingleLiveEvent<String> = SingleLiveEvent()
    val usernameError: SingleLiveEvent<String> = SingleLiveEvent()
    val passwordError: SingleLiveEvent<String> = SingleLiveEvent()
    val repeatPasswordError: SingleLiveEvent<String> = SingleLiveEvent()


    fun savePassword(passwordData: PasswordInputData) {
        if (verifyPasswordInputData(passwordData)) {
            viewModelScope.launch {
                val encryptedPassword = encryptString(passwordData.password)
                repository.savePassword(passwordData.toPasswordDTO(encryptedPassword))

                navigationCommand.value = NavigationCommand.Back
            }
        } else {
            //TODO
        }
    }

    fun updatePassword(passwordData: PasswordInputData) {
        if (verifyPasswordInputData(passwordData)) {
            viewModelScope.launch {
                repository.updatePassword(
                    passwordData.toPasswordDTO(
                        encryptString(passwordData.password)
                    )
                )

                navigationCommand.value = NavigationCommand.Back
            }
        } else {
            //TODO
        }
    }

    //TODO move to ViewModel. Rework, compare passwords etc
    private fun verifyPasswordInputData(passwordData: PasswordInputData): Boolean {
        val (website, username, password, repeatPassword) = passwordData

        return when {
            password.isBlankOrEmpty() -> {
                passwordError.value = app.getString(R.string.password_blank_or_empty_error)
                false
            }
            repeatPassword.isBlankOrEmpty() -> {
                repeatPasswordError.value = app.getString(R.string.password_blank_or_empty_error)
                false
            }
            password.length < PASSWORD_MIN_LENGTH -> {
                passwordError.value = app.getString(R.string.password_too_short)
                false
            }
            repeatPassword.length < PASSWORD_MIN_LENGTH -> {
                repeatPasswordError.value = app.getString(R.string.password_too_short)
                false
            }
            password != repeatPassword -> {
                passwordError.value = app.getString(R.string.password_do_not_match_error)
                repeatPasswordError.value = app.getString(R.string.password_do_not_match_error)
                false
            }

            username.isBlankOrEmpty() -> {
                usernameError.value = app.getString(R.string.username_empty_error)
                false
            }
            website.isBlankOrEmpty() -> {
                websiteError.value = app.getString(R.string.website_empty_error)
                false
            }

            else -> true //All good
        }
    }

    private fun encryptString(plainText: String): String {
        return cryptor.encryptString(plainText)
    }

    fun loadPassword(passwordId: Int) {
        viewModelScope.launch {
            val passwordDTO = repository.getPassword(passwordId)

            if (null != passwordDTO) {
                _password.postValue(passwordDTO.toPassword())
            } else {
                //TODO
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class PasswordAddEditViewModelFactory(
    private val repository: PasswordDataSource,
    private val configStorage: KeeperConfigStorage,
    private val cryptor: KeeperCryptor,
    private val app: Application
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (PasswordAddEditViewModel(repository, configStorage, cryptor, app) as T)

}