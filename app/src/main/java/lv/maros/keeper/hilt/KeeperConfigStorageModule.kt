package lv.maros.keeper.hilt

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import lv.maros.keeper.security.KeeperConfigStorage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KeeperConfigStorageModule {

    @Singleton
    @Provides
    fun provideKeeperConfigStorage(@ApplicationContext app: Context, @IoDispatcher ioDispatcher: CoroutineDispatcher) =
        KeeperConfigStorage(app, ioDispatcher)
}
