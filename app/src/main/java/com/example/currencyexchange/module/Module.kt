package com.example.currencyexchange.module

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.room.Room
import com.example.currencyexchange.BuildConfig
import com.example.currencyexchange.local.CurrencyDatabase
import com.example.currencyexchange.local.DATABASE_NAME
import com.example.currencyexchange.local.PREFERENCE_NAME
import com.example.currencyexchange.mapper.CurrencyMapper
import com.example.currencyexchange.mapper.CurrencyMapperImpl
import com.example.currencyexchange.mapper.LocalCacheMapper
import com.example.currencyexchange.mapper.LocalCacheMapperImpl
import com.example.currencyexchange.network.Api
import com.example.currencyexchange.repository.CurrencyExchangeRepository
import com.example.currencyexchange.repository.CurrencyExchangeRepositoryImpl
import com.example.currencyexchange.ui.exchange.viewModel.CurrencyExchangeViewModel
import com.example.currencyexchange.ui.list.viewmodel.CurrencyListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory

val netModule = module {
    factory { provideOkHttpClient() }
    factory { provideExchangeApi(get()) }
    single { provideRetrofit(get()) }
}

val repoModule = module {
    single { CurrencyExchangeRepositoryImpl(get(), get(), get(), get(), get()) } bind
        CurrencyExchangeRepository::class
}

val provideMapper = module {
    single { CurrencyMapperImpl() } bind CurrencyMapper::class
    single { LocalCacheMapperImpl() } bind LocalCacheMapper::class
}

val viewModelModule = module {
    viewModel { CurrencyListViewModel(get()) }
    viewModel { CurrencyExchangeViewModel(get()) }
}

val cacheModule = module {
    single {
        Room.databaseBuilder(
            get(),
            CurrencyDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
    single { get<CurrencyDatabase>().getCurrentExchangeDao() }
    single { get<Context>().getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE) }
}

fun provideOkHttpClient(): OkHttpClient {
    val client = OkHttpClient().newBuilder()
    if (BuildConfig.DEBUG) {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        client.addInterceptor(logger)

    }
    return client.build()
}

fun provideExchangeApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Builder().baseUrl(BuildConfig.BASE_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}