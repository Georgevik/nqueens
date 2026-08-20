package com.georgevik.nqueens.di

import android.content.Context
import androidx.room.Room
import com.georgevik.nqueens.data.storage.NQueensDatabase
import com.georgevik.nqueens.data.storage.ScoreDao
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
    fun provideDatabase(@ApplicationContext context: Context): NQueensDatabase =
        Room.databaseBuilder(context, NQueensDatabase::class.java, "nqueens.db")
            .build()

    @Provides
    fun provideScoreDao(database: NQueensDatabase): ScoreDao = database.scoreDao()
}
