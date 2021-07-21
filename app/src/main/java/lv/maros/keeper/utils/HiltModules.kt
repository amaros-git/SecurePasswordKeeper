package lv.maros.keeper.utils

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import lv.maros.keeper.authentication.KeeperAuthenticator
import lv.maros.keeper.security.KeeperConfigStorage
import lv.maros.keeper.security.KeeperCryptor
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object KeeperConfigStorageModule {

    @Singleton
    @Provides
    fun provideKeeperConfigStorage(@ApplicationContext app: Context) =
        KeeperConfigStorage(app)
}

@Module
@InstallIn(ActivityComponent::class)
object KeeperAuthenticatorModule {

    @Singleton
    @Provides
    fun provideKeeperConfigStorage(configStorage: KeeperConfigStorage) =
        KeeperAuthenticator(configStorage)
}


@Module
@InstallIn(ActivityComponent::class)
object KeeperCryptorModule {

    @Provides
    @Named
    fun provideKeeperCryptor() = KeeperCryptor()
}
