package com.example.network.di

import com.example.network.repository.FakeW3WDataSource
import com.example.network.repository.W3WDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindW3WDataSource(
        w3wDataSource: FakeW3WDataSource
    ) : W3WDataSource

}