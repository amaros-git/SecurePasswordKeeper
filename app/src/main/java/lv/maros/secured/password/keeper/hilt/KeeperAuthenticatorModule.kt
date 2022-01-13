package lv.maros.secured.password.keeper.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lv.maros.secured.password.keeper.authentication.KeeperAuthenticator
import lv.maros.secured.password.keeper.security.KeeperConfigStorage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KeeperAuthenticatorModule {

    @Singleton
    @Provides
    fun provideKeeperConfigStorage(configStorage: KeeperConfigStorage) =
        KeeperAuthenticator(configStorage)
}