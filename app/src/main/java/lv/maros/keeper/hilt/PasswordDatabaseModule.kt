package lv.maros.keeper.hilt

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import lv.maros.keeper.authentication.KeeperAuthenticator
import lv.maros.keeper.data.database.PasswordDatabase
import lv.maros.keeper.security.KeeperConfigStorage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PasswordDatabaseModule {

    @Singleton
    @Provides
    fun providePasswordDatabase(@ApplicationContext app: Context) =
        PasswordDatabase.getInstance(app)
}