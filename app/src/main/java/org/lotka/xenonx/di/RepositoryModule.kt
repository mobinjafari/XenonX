package org.lotka.xenonx.di

import AuthRemoteDataSource
import com.kilid.portal.data.repository.auth.AuthRepositoryImpl
import org.lotka.xenonx.data.repository.home.HomeRemoteDataSource
import org.lotka.xenonx.data.repository.home.HomeRepositoryImpl
import org.lotka.xenonx.domain.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.lotka.xenonx.domain.repository.AuthRepository
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {


    @Singleton
    @Provides
    fun provideHomeRepository(remoteDataSource: HomeRemoteDataSource): HomeRepository =
        HomeRepositoryImpl(remoteDataSource)

    @Singleton
    @Provides
    fun AuthRepository(remoteDataSource: AuthRemoteDataSource): AuthRepository =
        AuthRepositoryImpl(remoteDataSource)


}
