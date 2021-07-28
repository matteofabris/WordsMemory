package com.example.wordsmemory.di

import android.content.Context
import com.example.wordsmemory.database.WMDao
import com.example.wordsmemory.database.WMDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.InternalCoroutinesApi

@InstallIn(SingletonComponent::class)
@Module
object DbModule {

    @InternalCoroutinesApi
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): WMDatabase {
        return WMDatabase.getInstance(appContext)
    }

    @Provides
    fun provideVocabularyDao(database: WMDatabase): WMDao {
        return database.wmDao()
    }
}