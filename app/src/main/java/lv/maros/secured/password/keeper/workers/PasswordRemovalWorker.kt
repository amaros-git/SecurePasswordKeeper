package lv.maros.secured.password.keeper.workers

import android.app.Application
import androidx.work.Worker
import androidx.work.WorkerParameters
import lv.maros.secured.password.keeper.KeeperApplication
import lv.maros.secured.password.keeper.PASSWORD_LIST_TO_REMOVE_KEY
import lv.maros.secured.password.keeper.data.PasswordDataSource
import timber.log.Timber

class PasswordRemovalWorker(private val app: Application, params: WorkerParameters) :
    Worker(app.applicationContext, params) {

    val repository: PasswordDataSource = (app as KeeperApplication).localPasswordsRepository

    override fun doWork(): Result {

        val passwordIds = inputData.getIntArray(PASSWORD_LIST_TO_REMOVE_KEY)
        Timber.d("Got password ids tp remove: ")
        passwordIds?.forEach {
            Timber.d("$it")
        }

        return Result.success()

    }
}