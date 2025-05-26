package com.br.gitreposapp.di

import android.content.Context
import androidx.room.Room
import com.br.gitreposapp.data.RepoRepository
import com.br.gitreposapp.data.RepoRepositoryImpl
import com.br.gitreposapp.data.local.AppDatabase
import com.br.gitreposapp.data.local.RepoDao
import com.br.gitreposapp.data.remote.RepoApi
import com.br.gitreposapp.domain.usecases.GetFavoritesUseCase
import com.br.gitreposapp.domain.usecases.GetReposUseCase
import com.br.gitreposapp.domain.usecases.ObserveFavoritesUseCase
import com.br.gitreposapp.domain.usecases.ToggleFavoriteUseCase
import com.br.gitreposapp.ui.viewmodel.RepoViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ========== Provê instâncias de Rede ==========
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .build()
                chain.proceed(newRequest)
            }
            .addInterceptor(loggingInterceptor)
            .build()

        return client
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideGitHubApi(retrofit: Retrofit): RepoApi {
        return retrofit.create(RepoApi::class.java)
    }

    // ========== Provê instâncias de Banco de Dados ==========
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "github-db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideRepoDao(database: AppDatabase): RepoDao {
        return database.repoDao()
    }

    // ========== Provê Repositórios ==========
    @Provides
    fun provideRepoRepository(
        api: RepoApi,
        dao: RepoDao
    ): RepoRepository {
        return RepoRepositoryImpl(api, dao)
    }

    // ========== Provê Use Cases ==========
    @Provides
    fun provideGetReposUseCase(repository: RepoRepository): GetReposUseCase {
        return GetReposUseCase(repository)
    }

    @Provides
    fun provideToggleFavoriteUseCase(repository: RepoRepository): ToggleFavoriteUseCase {
        return ToggleFavoriteUseCase(repository)
    }

    @Provides
    fun provideObserveFavoritesUseCase(repository: RepoRepository): ObserveFavoritesUseCase {
        return ObserveFavoritesUseCase(repository)
    }

    @Provides
    fun provideGetFavoritesUseCase(repository: RepoRepository): GetFavoritesUseCase {
        return GetFavoritesUseCase(repository)
    }

    // ========== Provê ViewModels (para @ViewModelComponent) ==========
    @Module
    @InstallIn(ViewModelComponent::class)
    object ViewModelModule {
        @Provides
        fun provideRepoViewModel(
            getReposUseCase: GetReposUseCase,
            toggleFavoriteUseCase: ToggleFavoriteUseCase,
            observeFavoritesUseCase: ObserveFavoritesUseCase
        ): RepoViewModel {
            return RepoViewModel(getReposUseCase, toggleFavoriteUseCase, observeFavoritesUseCase)
        }
    }
}