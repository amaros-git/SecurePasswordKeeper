package lv.maros.secured.password.keeper.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lv.maros.secured.password.keeper.security.KeeperCryptor
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
object KeeperCryptorModule {

    @Provides
    @Named
    fun provideKeeperCryptor() = KeeperCryptor()
}