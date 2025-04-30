
package com.groupec.cleanarchitecturesampleapp.core.data.di

import android.content.Context
import com.groupec.cleanarchitecturesampleapp.core.data.repository.AttachmentRepository
import com.groupec.cleanarchitecturesampleapp.core.data.repository.AttachmentRepositoryImpl
import com.groupec.cleanarchitecturesampleapp.core.network.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule  {
    @Provides
    @Singleton
    fun providerAttachment(
        @ApplicationContext context: Context,
        apiService: ApiService
    ) : AttachmentRepository {
        return AttachmentRepositoryImpl(context, apiService)
    }
}