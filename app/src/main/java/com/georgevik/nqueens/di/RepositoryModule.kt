package com.georgevik.nqueens.di

import com.georgevik.nqueens.data.ScoreRepositoryImpl
import com.georgevik.nqueens.domain.repository.ScoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindScoreRepository(impl: ScoreRepositoryImpl): ScoreRepository
}
