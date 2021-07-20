package lv.maros.keeper.utils

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import lv.maros.keeper.security.KeeperConfigStorage
import lv.maros.keeper.security.KeeperCryptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KeeperConfigStorageModule {

    @Singleton
    @Provides
    fun provideKeeperConfigStorage(@ApplicationContext app: Application) =
        KeeperConfigStorage(app)
}