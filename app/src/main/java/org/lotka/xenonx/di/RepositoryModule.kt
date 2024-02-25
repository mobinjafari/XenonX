package org.lotka.xenonx.di

import org.lotka.xenonx.data.repository.home.HomeRemoteDataSource
import org.lotka.xenonx.data.repository.home.HomeRepositoryImpl
import org.lotka.xenonx.domain.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {



    @Provides
    fun provideHomeRepository(remoteDataSource: HomeRemoteDataSource): HomeRepository =
        HomeRepositoryImpl(remoteDataSource)

}
