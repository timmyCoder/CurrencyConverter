package com.example.currencyconverter.di

import android.content.Context
import com.example.currencyconverter.newtwork.ApiInterface
import com.example.currencyconverter.newtwork.CurrencyRemoteDataSource
import com.example.currencyconverter.room.dao.CurrencyDao
import com.example.currencyconverter.room.database.AppDatabase
import com.example.currencyconverter.room.repository.CurrencyRepository
import com.example.currencyconverter.utils.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = BASE_URL

    @Singleton
    @Provides
    fun retrofit(gson: Gson):
            Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideCurrencyService(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @Singleton
    @Provides
    fun provideCurrencyRemoteDataSource(apiInterface: ApiInterface) =
        CurrencyRemoteDataSource(apiInterface)

    @Singleton
    @Provides
    fun provideCurrencyDao(database: AppDatabase) =
        database.currencyDao()

    @Singleton
    @Provides
    fun provideCurrencyRepository(
        remoteDataSource: CurrencyRemoteDataSource,
        localDataSource: CurrencyDao
    ) =
        CurrencyRepository(remoteDataSource, localDataSource)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)
}