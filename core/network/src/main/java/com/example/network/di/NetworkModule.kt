package com.example.network.di

import com.example.network.repository.FakeW3WDataSource
import com.example.network.repository.W3WDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    fun bindW3WDataSource(
        w3wRemoteSource: FakeW3WDataSource
    ) : W3WDataSource

}