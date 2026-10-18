package com.example.data.model

import com.squareup.moshi.JsonClass

enum class UserRole {
    ADMIN,
    CUSTOMER,
    STORE,
    DRIVER
}

enum class UserStatus {
    ACTIVE,
    INACTIVE
}

@JsonClass(generateAdapter = true)
data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: UserRole,
    val status: UserStatus,
    val createdAt: String
)

@JsonClass(generateAdapter = true)
data class Store(
    val id: String,
    val nameEn: String,
    val nameAr: String,
    val ownerName: String,
    val categoryName: String,
    val phone: String,
    val address: String,
    val isOpen: Boolean,
    val isActive: Boolean,
    val totalOrders: Int = 0,
    val rating: Double = 4.8
)

enum class DriverAvailability {
    AVAILABLE,
    BUSY,
    OFFLINE
}

@JsonClass(generateAdapter = true)
data class Driver(
    val id: String,
    val name: String,
    val phone: String,
    val availability: DriverAvailability,
    val isActive: Boolean,
    val currentOrderId: String? = null,
    val completedDeliveries: Int = 0,
    val vehicleType: String = "Motorcycle",
    val rating: Double = 4.9
)

@JsonClass(generateAdapter = true)
data class Category(
    val id: String,
    val nameEn: String,
    val nameAr: String,
    val iconName: String,
    val isActive: Boolean = true,
    val storeCount: Int = 0
)

@JsonClass(generateAdapter = true)
data class Product(
    val id: String,
    val storeId: String,
    val storeName: String,
    val categoryId: String,
    val categoryName: String,
    val nameEn: String,
    val nameAr: String,
    val price: Double,
    val isAvailable: Boolean,
    val isActive: Boolean,
    val description: String = ""
)

enum class OrderStatus {
    NEW,
    ACCEPTED,
    PREPARING,
    READY,
    PICKED_UP,
    ON_THE_WAY,
    DELIVERED,
    REJECTED,
    CANCELLED;

    fun isTerminal(): Boolean = this == DELIVERED || this == REJECTED || this == CANCELLED
    fun isActiveOrder(): Boolean = !isTerminal()
}

@JsonClass(generateAdapter = true)
data class OrderItem(
    val productId: String,
    val productNameEn: String,
    val productNameAr: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double = quantity * unitPrice
)

@JsonClass(generateAdapter = true)
data class Order(
    val id: String,
    val orderNumber: String,
    val customerId: String,
    val customerName: String,
    val customerPhone: String,
    val storeId: String,
    val storeName: String,
    val driverId: String?,
    val driverName: String?,
    val items: List<OrderItem>,
    val subtotal: Double,
    val deliveryFee: Double,
    val total: Double,
    val address: String,
    val status: OrderStatus,
    val createdAt: String,
    val updatedAt: String,
    val paymentMethod: String = "Card",
    val notes: String = ""
)

@JsonClass(generateAdapter = true)
data class DashboardMetrics(
    val totalOrders: Int,
    val todayOrders: Int,
    val activeOrders: Int,
    val deliveredOrders: Int,
    val cancelledOrders: Int,
    val totalStores: Int,
    val activeStores: Int,
    val totalDrivers: Int,
    val availableDrivers: Int,
    val customers: Int,
    val totalRevenue: Double
)

@JsonClass(generateAdapter = true)
data class SystemSettings(
    val commissionRatePercent: Double = 0.0,
    val baseDeliveryFee: Double = 0.00,
    val feePerKm: Double = 0.0,
    val taxRatePercent: Double = 0.0,
    val autoAssignDrivers: Boolean = false,
    val maxDeliveryRadiusKm: Double = 25.0,
    val maintenanceMode: Boolean = false,
    val currencySymbol: String = "SDG",
    val isArabicRtl: Boolean = true
)

@JsonClass(generateAdapter = true)
data class AdminUser(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole = UserRole.ADMIN,
    val token: String = "",
    val permissions: List<String> = listOf("ALL", "ORDERS", "USERS", "STORES", "DRIVERS", "SETTINGS")
)

@JsonClass(generateAdapter = true)
data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null
)
