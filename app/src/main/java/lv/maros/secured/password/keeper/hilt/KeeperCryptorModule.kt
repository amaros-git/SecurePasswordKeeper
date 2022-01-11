package lv.maros.keeper.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lv.maros.keeper.security.KeeperCryptor
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
object KeeperCryptorModule {

    @Provides
    @Named
    fun provideKeeperCryptor() = KeeperCryptor()
}