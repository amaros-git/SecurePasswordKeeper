package lv.maros.keeper.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lv.maros.keeper.authentication.KeeperAuthenticator
import lv.maros.keeper.security.KeeperConfigStorage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KeeperAuthenticatorModule {

    @Singleton
    @Provides
    fun provideKeeperConfigStorage(configStorage: KeeperConfigStorage) =
        KeeperAuthenticator(configStorage)
}