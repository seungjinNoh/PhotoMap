package com.example.data.di

import com.example.data.impl.PhotoRepositoryImpl
import com.example.data.impl.W3WRepositoryImpl
import com.example.domain.repository.PhotoRepository
import com.example.domain.repository.W3WRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindW3WRepository(
        w3WRepository: W3WRepositoryImpl
    ) : W3WRepository

    @Binds
    @Singleton
    abstract fun bindPhotoRepository(
        photoRepository: PhotoRepositoryImpl
    ) : PhotoRepository

}