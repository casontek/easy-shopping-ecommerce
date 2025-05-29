package com.casontek.easyshop.di

import android.content.Context
import androidx.room.Room
import com.casontek.easyshop.data.local.AgroDatabase
import com.casontek.easyshop.data.local.dao.ProductDao
import com.casontek.easyshop.data.local.dao.ReviewDao
import com.casontek.easyshop.data.remote.api.RestApiService
import com.casontek.easyshop.data.repository.ProductRepository
import com.casontek.easyshop.data.repository.ProductRepositoryImpl
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
    fun providesDatabase(@ApplicationContext context: Context) : AgroDatabase {
        return Room.databaseBuilder(
            context,
            AgroDatabase::class.java,
            "agro_db"
        ).fallbackToDestructiveMigration(true)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun providesProductDao(database: AgroDatabase) = database.productDao()

    @Provides
    @Singleton
    fun providesReviewDao(database: AgroDatabase) = database.reviewDao()

    @Provides
    @Singleton
    fun providesCardDao(database: AgroDatabase) = database.cartDao()

    @Provides
    @Singleton
    fun providesProductRepository(
        productDao: ProductDao,
        reviewDao: ReviewDao,
        restApiService: RestApiService,
        @ApplicationContext context: Context
    ) : ProductRepository = ProductRepositoryImpl(
        restApiService,
        productDao,
        reviewDao,
        context
    )

}