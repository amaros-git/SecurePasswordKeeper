package lv.maros.keeper.pages.passwords

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import lv.maros.keeper.R
import lv.maros.keeper.data.local.PasswordDatabase
import lv.maros.keeper.data.dto.PasswordDTO
import lv.maros.keeper.models.Password
import lv.maros.keeper.models.PasswordInputData
import lv.maros.keeper.security.KeeperConfigStorage
import lv.maros.keeper.security.KeeperCryptor
import lv.maros.keeper.utils.KeeperResult
import lv.maros.keeper.utils.SingleLiveEvent
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PasswordsViewModel @Inject constructor(
    private val passwordDb: PasswordDatabase,
    app: Application
) : AndroidViewModel(app) {

    val showNoData: MutableLiveData<Boolean> = MutableLiveData()

    private val _passwordList = MutableLiveData<List<Password>>()
    val passwordList: LiveData<List<Password>>
        get() = _passwordList


    fun loadPassword() {
        viewModelScope.launch(Dispatchers.Main) {
            val savedPasswords = passwordDb.passwordDao.getAllPasswords()

            if (savedPasswords.isNotEmpty()) {
                val passwordToShow = ArrayList<Password>()
                passwordToShow.addAll(savedPasswords.map { passwordDto ->
                    passwordDtoToPassword(passwordDto)
                })

                _passwordList.value = passwordToShow
            }

            //check if no data image has to be shown
            invalidateShowNoData()
        }
    }


    private fun passwordDtoToPassword(passwordDto: PasswordDTO) =
        Password(
            passwordDto.website,
            passwordDto.username,
            passwordDto.encryptedPassword,
            passwordDto.passwordLastModificationDate,
            passwordDto.id
        )

    /**
     * Inform the user that there's not any data if the remindersList is empty
     */
    private fun invalidateShowNoData() {
        showNoData.value = passwordList.value == null || passwordList.value!!.isEmpty()
    }

}