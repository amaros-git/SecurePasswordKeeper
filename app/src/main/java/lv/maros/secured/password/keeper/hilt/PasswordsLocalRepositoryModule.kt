package lv.maros.secured.password.keeper.hilt

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import lv.maros.secured.password.keeper.data.local.PasswordDatabase
import lv.maros.secured.password.keeper.data.local.PasswordsLocalRepository
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
object PasswordsLocalRepositoryModule {

    @Provides
    @Named
    fun providePasswordsLocalRepository(@ApplicationContext app: Context) =
        PasswordsLocalRepository(
            PasswordDatabase.getInstance(app)
        )
}