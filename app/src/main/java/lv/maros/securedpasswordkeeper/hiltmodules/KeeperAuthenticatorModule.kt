package lv.maros.securedpasswordkeeper.hiltmodules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lv.maros.securedpasswordkeeper.authentication.KeeperAuthenticator

@Module
@InstallIn(SingletonComponent::class)
class KeeperAuthenticatorModule {

    @Provides
    fun provideAuthenticator() = KeeperAuthenticator()
}