package lv.maros.secured.password.keeper.pages.passwords

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.launch
import lv.maros.secured.password.keeper.PASSWORDS_REMOVAL_TAG
import lv.maros.secured.password.keeper.PASSWORD_IDS_TO_REMOVE_KEY
import lv.maros.secured.password.keeper.PASSWORD_REMOVAL_WORKER_INITIAL_DELAY
import lv.maros.secured.password.keeper.R
import lv.maros.secured.password.keeper.base.BaseViewModel
import lv.maros.secured.password.keeper.data.PasswordDataSource
import lv.maros.secured.password.keeper.models.PasswordDTO
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.security.KeeperCryptor
import lv.maros.secured.password.keeper.utils.toPassword
import lv.maros.secured.password.keeper.workers.PasswordRemovalCoroutineWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class PasswordsViewModel(
    private val repository: PasswordDataSource,
    private val cryptor: KeeperCryptor,
    private val app: Application
) : BaseViewModel(app) {

    val showNoData: MutableLiveData<Boolean> = MutableLiveData()

    private val _passwordList = MutableLiveData<MutableList<Password>>()
    val passwordList: LiveData<MutableList<Password>>
        get() = _passwordList

    private val workManager = WorkManager.getInstance(app.applicationContext)

    init {
        TEST_load_password_if_empty()

        loadAllPasswords()
    }

    fun TEST_load_password_if_empty() {
        viewModelScope.launch {
            val passwords = repository.getAllPasswords()
            if(passwords.isEmpty()) {
                for (i in 0 .. 9) {
                    repository.savePassword(
                        PasswordDTO(
                            website = "website$i",
                            username = "username$i",
                            encryptedPassword = cryptor.encryptString("password$i"),
                            passwordLastModificationDate = 12301L,
                            0
                        )
                    )
                }
            }
        }
    }

    /**
     * Inform the user that there's not any data if the remindersList is empty
     */
    private fun invalidateShowNoData() {
        showNoData.value = _passwordList.value == null || _passwordList.value!!.isEmpty()
    }


    private fun removePasswordItem(passwordListAdapter: PasswordListAdapter, swipedPos: Int) {
        _passwordList.value?.removeAt(swipedPos)
        passwordListAdapter.notifyItemRemoved(swipedPos)

        invalidateShowNoData()
    }

    private fun addPasswordItem(
        passwordListAdapter: PasswordListAdapter,
        password: Password,
        swipedPos: Int
    ) {
        _passwordList.value?.add(swipedPos, password)
        passwordListAdapter.notifyItemInserted(swipedPos)

        invalidateShowNoData()
    }

    /**
     * for such type of application I consider this as unique enough
     */
    private fun generateWorkRequestTag(passwordsIds: IntArray): String {
        val tag =
            PASSWORDS_REMOVAL_TAG + "_" + passwordsIds.sum().toString() + "_" + Random.nextInt(
                0,
                Int.MAX_VALUE
            )
                .toString()

        Timber.d("tag = $tag")
        return tag
    }

    internal fun loadAllPasswords() {
        viewModelScope.launch {
            val passwords = repository.getAllPasswords()

            if (passwords.isNotEmpty()) {
                _passwordList.value =
                    passwords.map { it.toPassword() }.sortedBy { it.website }.toMutableList()
            }

            invalidateShowNoData()
        }
    }

    internal fun decryptString(data: String): String {
        val decryptedData = cryptor.decryptString(data)
        return if (null != decryptedData) {
            decryptedData
        } else {
            showErrorMessage.value = (app.getString(R.string.internal_error))
            ""
        }
    }

    internal fun undoPasswordsRemoval(
        passwordListAdapter: PasswordListAdapter,
        password: Password,
        swipedPos: Int,
        workRequestTag: String
    ) {
        workManager.cancelAllWorkByTag(workRequestTag)
        addPasswordItem(passwordListAdapter, password, swipedPos)
    }

    /**
     * returns work request tag
     */
    internal fun deletePasswords(
        passwordListAdapter: PasswordListAdapter,
        swipedPos: Int,
        passwordIds: IntArray
    ): String {
        removePasswordItem(passwordListAdapter, swipedPos)

        val data = workDataOf(PASSWORD_IDS_TO_REMOVE_KEY to passwordIds)

        val tag = generateWorkRequestTag(passwordIds)

        val workRequest = OneTimeWorkRequestBuilder<PasswordRemovalCoroutineWorker>()
            .setInputData(data)
            .setInitialDelay(PASSWORD_REMOVAL_WORKER_INITIAL_DELAY, TimeUnit.MILLISECONDS)
            .addTag(tag)
            .build()

        workManager.enqueue(workRequest)

        return tag
    }

    internal fun getPasswordsList(): List<Password>? = _passwordList.value?.toList()

    internal fun copyToClipboard(text: String) {
        Timber.d("copyToClipboard: $text") //TODO REMOVE IT !!!!!!!!!!!!!!!!!!!
        val clipboard = app.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(
            ClipData.newPlainText(
                "SecurePasswordKeeper",
                text
            )
        )
    }
}

@Suppress("UNCHECKED_CAST")
class PasswordsViewModelFactory(
    private val repository: PasswordDataSource,
    private val cryptor: KeeperCryptor,
    private val app: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (PasswordsViewModel(repository, cryptor, app) as T)

}