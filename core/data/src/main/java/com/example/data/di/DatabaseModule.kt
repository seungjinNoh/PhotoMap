package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.database.PhotoDao
import com.example.data.database.PhotoDatabase
import com.example.data.repository.DefaultPhotoRepository
import com.example.data.repository.PhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PhotoDatabase =
        Room.databaseBuilder(context, PhotoDatabase::class.java, "photo_db").build()

    @Provides
    fun providePhotoDao(db: PhotoDatabase): PhotoDao = db.photoDao()

    @Provides
    fun providePhotoRepository(dao: PhotoDao): PhotoRepository = DefaultPhotoRepository(dao = dao)

}