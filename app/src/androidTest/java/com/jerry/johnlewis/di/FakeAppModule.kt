package com.jerry.johnlewis.di

import com.google.gson.GsonBuilder
import com.jerry.johnlewis.constants.TIME_OUT
import com.jerry.johnlewis.network.ProductApi



import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
@Module
class FakeAppModule {

    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit): ProductApi {
        return retrofit.create(ProductApi::class.java)
    }



    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)


        builder.addInterceptor(interceptor)


        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .client(builder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("http://127.0.0.1:8080")
            .build()
    }
}