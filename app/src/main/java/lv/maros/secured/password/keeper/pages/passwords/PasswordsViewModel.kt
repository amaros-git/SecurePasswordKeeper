package lv.maros.secured.password.keeper.pages.passwords

import android.app.Application
import androidx.lifecycle.*
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.launch
import lv.maros.secured.password.keeper.*
import lv.maros.secured.password.keeper.base.BaseViewModel
import lv.maros.secured.password.keeper.data.PasswordDataSource
import lv.maros.secured.password.keeper.models.Password
import lv.maros.secured.password.keeper.security.KeeperCryptor
import lv.maros.secured.password.keeper.utils.toPassword
import lv.maros.secured.password.keeper.workers.PasswordRemovalCoroutineWorker
import java.util.concurrent.TimeUnit

class PasswordsViewModel(
    private val repository: PasswordDataSource,
    private val cryptor: KeeperCryptor,
    private val app: Application
) : BaseViewModel(app) {

    val showNoData: MutableLiveData<Boolean> = MutableLiveData()

    private val _passwordList = MutableLiveData<MutableList<Password>>()
    val passwordList: LiveData<MutableList<Password>>
        get() = _passwordList

    //private val passwordsToDelete = mutableListOf<Password>()

    private val workManager = WorkManager.getInstance(app.applicationContext)

    /**
     * Inform the user that there's not any data if the remindersList is empty
     */
    private fun invalidateShowNoData() {
        showNoData.value = _passwordList.value == null || _passwordList.value!!.isEmpty()
    }


    private fun removePasswordItem(passwordListAdapter: PasswordListAdapter, swipedPos: Int) {
        _passwordList.value?.removeAt(swipedPos)
        passwordListAdapter.notifyItemRemoved(swipedPos)
    }

    private fun addPasswordItem(
        passwordListAdapter: PasswordListAdapter,
        password: Password,
        swipedPos: Int
    ) {
        _passwordList.value?.add(swipedPos, password)
        passwordListAdapter.notifyItemInserted(swipedPos)
    }

    private fun generateUniqueWorkName(): String {

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
        swipedPos: Int
    ) {
        workManager.cancelUniqueWork(PASSWORDS_REMOVAL_WORK_NAME)
        addPasswordItem(passwordListAdapter, password, swipedPos)
    }

    internal fun deletePasswords(
        passwordListAdapter: PasswordListAdapter,
        swipedPos: Int,
        passwordIds: IntArray,
    ) {
        removePasswordItem(passwordListAdapter, swipedPos)

        val data = workDataOf(PASSWORD_IDS_TO_REMOVE_KEY to passwordIds)

        val workRequest = OneTimeWorkRequestBuilder<PasswordRemovalCoroutineWorker>()
            .setInputData(data)
            .setInitialDelay(PASSWORD_REMOVAL_WORKER_INITIAL_DELAY, TimeUnit.MILLISECONDS)
            .addTag(PASSWORDS_REMOVAL_TAG)
            .build()

        workManager.enqueueUniqueWork(
            PASSWORDS_REMOVAL_WORK_NAME,
            ExistingWorkPolicy.APPEND_OR_REPLACE, //TODO test and think which policy is the best here
            workRequest
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