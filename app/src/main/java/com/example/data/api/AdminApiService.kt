package com.example.data.api

import com.example.data.model.Category
import com.example.data.model.DashboardMetrics
import com.example.data.model.Driver
import com.example.data.model.Order
import com.example.data.model.Product
import com.example.data.model.Store
import com.example.data.model.SystemSettings
import com.example.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AdminApiService {

    @GET("api/admin/dashboard")
    suspend fun getDashboard(): Response<DashboardMetrics>

    @GET("api/admin/users")
    suspend fun getUsers(@Query("role") role: String? = null): Response<List<User>>

    @PUT("api/admin/users/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body user: User
    ): Response<User>

    @GET("api/admin/stores")
    suspend fun getStores(): Response<List<Store>>

    @PUT("api/admin/stores/{id}")
    suspend fun updateStore(
        @Path("id") id: String,
        @Body store: Store
    ): Response<Store>

    @GET("api/admin/drivers")
    suspend fun getDrivers(): Response<List<Driver>>

    @PUT("api/admin/drivers/{id}")
    suspend fun updateDriver(
        @Path("id") id: String,
        @Body driver: Driver
    ): Response<Driver>

    @GET("api/admin/categories")
    suspend fun getCategories(): Response<List<Category>>

    @POST("api/admin/categories")
    suspend fun createCategory(
        @Body category: Category
    ): Response<Category>

    @PUT("api/admin/categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: String,
        @Body category: Category
    ): Response<Category>

    @DELETE("api/admin/categories/{id}")
    suspend fun deleteCategory(
        @Path("id") id: String
    ): Response<Unit>

    @GET("api/admin/products")
    suspend fun getProducts(): Response<List<Product>>

    @PUT("api/admin/products/{id}")
    suspend fun updateProduct(
        @Path("id") id: String,
        @Body product: Product
    ): Response<Product>

    @GET("api/admin/orders")
    suspend fun getOrders(
        @Query("status") status: String? = null
    ): Response<List<Order>>

    @GET("api/admin/orders/{id}")
    suspend fun getOrderById(
        @Path("id") id: String
    ): Response<Order>

    @PUT("api/admin/orders/{id}")
    suspend fun updateOrder(
        @Path("id") id: String,
        @Body order: Order
    ): Response<Order>

    @GET("api/admin/settings")
    suspend fun getSettings(): Response<SystemSettings>

    @PUT("api/admin/settings")
    suspend fun updateSettings(
        @Body settings: SystemSettings
    ): Response<SystemSettings>
}
