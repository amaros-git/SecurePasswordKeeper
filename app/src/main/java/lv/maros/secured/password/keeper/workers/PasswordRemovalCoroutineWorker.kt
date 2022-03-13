package lv.maros.secured.password.keeper.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lv.maros.secured.password.keeper.KeeperApplication
import lv.maros.secured.password.keeper.PASSWORD_IDS_TO_REMOVE_KEY
import lv.maros.secured.password.keeper.data.PasswordDataSource
import timber.log.Timber

class PasswordRemovalCoroutineWorker(private val ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params) {

    private val repository: PasswordDataSource =
        (ctx.applicationContext as KeeperApplication).localPasswordsRepository

    private suspend fun deletePasswords(passwordsIds: IntArray) {
        withContext(Dispatchers.IO) {
            passwordsIds.forEach {
                repository.deletePassword(it)
            }
        }
    }

    override suspend fun doWork(): Result {
        /* Timber.d("Got password ids tp remove: ")
           passwordIds.forEach {
               Timber.d("$it")
           }*/
        val passwordIds = inputData.getIntArray(PASSWORD_IDS_TO_REMOVE_KEY)
        if (null != passwordIds) {
            deletePasswords(passwordIds)
        } else {
            Timber.e("passwordIds is null")
            Result.failure()
        }

        return Result.success()
    }
}