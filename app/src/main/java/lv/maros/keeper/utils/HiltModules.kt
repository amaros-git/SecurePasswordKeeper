package lv.maros.keeper.utils

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import lv.maros.keeper.authentication.KeeperAuthenticator
import lv.maros.keeper.security.KeeperConfigStorage
import lv.maros.keeper.security.KeeperCryptor
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KeeperConfigStorageModule {

    @Singleton
    @Provides
    fun provideKeeperConfigStorage(@ApplicationContext app: Context, @IoDispatcher ioDispatcher: CoroutineDispatcher) =
        KeeperConfigStorage(app, ioDispatcher)
}

@Module
@InstallIn(SingletonComponent::class)
object KeeperAuthenticatorModule {

    @Singleton
    @Provides
    fun provideKeeperConfigStorage(configStorage: KeeperConfigStorage) =
        KeeperAuthenticator(configStorage)
}


@Module
@InstallIn(SingletonComponent::class)
object KeeperCryptorModule {

    @Provides
    @Named
    fun provideKeeperCryptor() = KeeperCryptor()
}

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher
