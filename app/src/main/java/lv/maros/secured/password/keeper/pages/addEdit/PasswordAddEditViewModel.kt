package lv.maros.secured.password.keeper.pages.addEdit

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.base.BaseViewModel
import lv.maros.secured.password.keeper.base.NavigationCommand
import lv.maros.secured.password.keeper.data.PasswordDataSource
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.models.PasswordInputData
import lv.maros.secured.password.keeper.security.KeeperCryptor
import lv.maros.secured.password.keeper.utils.*
import timber.log.Timber
import kotlin.properties.Delegates

class PasswordAddEditViewModel(
    private val repository: PasswordDataSource,
    private val cryptor: KeeperCryptor,
    private val app: Application
) : BaseViewModel(app) {

    private val _passwordToEdit = MutableLiveData<Password>()
    internal val passwordToEdit: LiveData<Password>
        get() = _passwordToEdit

    private val _generatedPassword = MutableLiveData<String>()
    internal val generatedPassword: LiveData<String>
        get() = _generatedPassword

    internal val websiteError: SingleLiveEvent<String> = SingleLiveEvent()
    internal val usernameError: SingleLiveEvent<String> = SingleLiveEvent()
    internal val passwordError: SingleLiveEvent<String> = SingleLiveEvent()
    internal val repeatPasswordError: SingleLiveEvent<String> = SingleLiveEvent()


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

            website.isEmpty() -> {
                websiteError.value = app.getString(R.string.website_empty_error)
                false
            }

            else -> true //All good
        }
    }

    private fun encryptString(data: String): String {
        return cryptor.encryptString(data)
    }

    override fun onCleared() {
        Timber.d("onCleared called")
    }

    internal fun savePassword(passwordData: PasswordInputData) {
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

    internal fun updatePassword(passwordData: PasswordInputData, passwordId: Int) {
        if (verifyPasswordInputData(passwordData)) {
            viewModelScope.launch {
                val passwordDTO = passwordData.toPasswordDTO(encryptString(passwordData.password))
                    .apply { id = passwordId }

                repository.updatePassword(passwordDTO)

                navigationCommand.value = NavigationCommand.Back
            }
        }
    }

    internal fun decryptString(data: String): String? {
        return cryptor.decryptString(data)
    }

    internal  fun loadPassword(passwordId: Int) {
        viewModelScope.launch {
            val passwordDTO = repository.getPassword(passwordId)

            if (null != passwordDTO) {
                _passwordToEdit.postValue(passwordDTO.toPassword())
            } else {
                Timber.e("Password with id $passwordId doesn't exist")
            }
        }
    }

    internal fun saveGeneratedPassword(password: String) {
        _generatedPassword.value = password
    }
}

@Suppress("UNCHECKED_CAST")
class PasswordAddEditViewModelFactory(
    private val repository: PasswordDataSource,
    private val cryptor: KeeperCryptor,
    private val app: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (PasswordAddEditViewModel(repository, cryptor, app) as T)

}