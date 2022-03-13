package lv.maros.secured.password.keeper.workers

import android.app.Application
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import lv.maros.secured.password.keeper.KeeperApplication
import lv.maros.secured.password.keeper.PASSWORD_IDS_TO_REMOVE_KEY
import lv.maros.secured.password.keeper.data.PasswordDataSource
import timber.log.Timber

class PasswordRemovalWorker(ctx: Context, params: WorkerParameters) :
    Worker(ctx, params) {

    //val repository: PasswordDataSource = (app as KeeperApplication).localPasswordsRepository

    override fun doWork(): Result {

        val passwordIds = inputData.getIntArray(PASSWORD_IDS_TO_REMOVE_KEY)
        Timber.d("Got password ids tp remove: ")
        passwordIds?.forEach {
            Timber.d("$it")
        }

        return Result.success()

    }
}