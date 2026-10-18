package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AdminUser
import com.example.data.model.Category
import com.example.data.model.DashboardMetrics
import com.example.data.model.Driver
import com.example.data.model.DriverAvailability
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.Product
import com.example.data.model.Store
import com.example.data.model.SystemSettings
import com.example.data.model.User
import com.example.data.model.UserRole
import com.example.data.repository.AdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AdminScreen {
    LOGIN,
    DASHBOARD,
    ORDERS,
    ORDER_DETAILS,
    USERS,
    STORES,
    DRIVERS,
    CATEGORIES,
    PRODUCTS,
    SETTINGS,
    PROFILE
}

class AdminViewModel(
    val repository: AdminRepository = AdminRepository()
) : ViewModel() {

    // Navigation state
    private val _currentScreen = MutableStateFlow(AdminScreen.LOGIN)
    val currentScreen: StateFlow<AdminScreen> = _currentScreen.asStateFlow()

    private val screenBackStack = mutableListOf<AdminScreen>()

    // Arabic RTL state
    private val _isArabic = MutableStateFlow(false)
    val isArabic: StateFlow<Boolean> = _isArabic.asStateFlow()

    // Auth state
    private val _loginEmail = MutableStateFlow("")
    val loginEmail: StateFlow<String> = _loginEmail.asStateFlow()

    private val _loginPassword = MutableStateFlow("")
    val loginPassword: StateFlow<String> = _loginPassword.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val _isAuthenticating = MutableStateFlow(false)
    val isAuthenticating: StateFlow<Boolean> = _isAuthenticating.asStateFlow()

    val currentAdmin: StateFlow<AdminUser?> = repository.currentAdmin

    // Dashboard metrics
    private val _metrics = MutableStateFlow<DashboardMetrics?>(null)
    val metrics: StateFlow<DashboardMetrics?> = _metrics.asStateFlow()

    // Base data streams
    val allOrders = repository.orders
    val allUsers = repository.users
    val allStores = repository.stores
    val allDrivers = repository.drivers
    val allCategories = repository.categories
    val allProducts = repository.products
    val settings = repository.settings

    // Order filters
    val orderSearchQuery = MutableStateFlow("")
    val orderStatusFilter = MutableStateFlow<OrderStatus?>(null)
    val orderStoreFilter = MutableStateFlow<String?>(null)
    val orderDriverFilter = MutableStateFlow<String?>(null)
    val orderCustomerFilter = MutableStateFlow<String?>(null)
    val selectedOrder = MutableStateFlow<Order?>(null)

    val filteredOrders: StateFlow<List<Order>> = combine(
        combine(allOrders, orderSearchQuery, orderStatusFilter) { orders, query, status ->
            Triple(orders, query, status)
        },
        combine(orderStoreFilter, orderDriverFilter, orderCustomerFilter) { store, driver, customer ->
            Triple(store, driver, customer)
        }
    ) { (orders, query, status), (store, driver, customer) ->
        orders.filter { order ->
            val matchesQuery = query.isBlank() ||
                order.orderNumber.contains(query, ignoreCase = true) ||
                order.customerName.contains(query, ignoreCase = true) ||
                order.storeName.contains(query, ignoreCase = true) ||
                (order.driverName?.contains(query, ignoreCase = true) == true)

            val matchesStatus = status == null || order.status == status
            val matchesStore = store == null || order.storeId == store
            val matchesDriver = driver == null || order.driverId == driver
            val matchesCustomer = customer == null || order.customerId == customer

            matchesQuery && matchesStatus && matchesStore && matchesDriver && matchesCustomer
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Users filters
    val userSearchQuery = MutableStateFlow("")
    val userRoleFilter = MutableStateFlow<UserRole?>(null)
    val filteredUsers: StateFlow<List<User>> = combine(
        allUsers,
        userSearchQuery,
        userRoleFilter
    ) { users, query, role ->
        users.filter { user ->
            val matchesQuery = query.isBlank() ||
                user.name.contains(query, ignoreCase = true) ||
                user.email.contains(query, ignoreCase = true) ||
                user.phone.contains(query, ignoreCase = true)
            val matchesRole = role == null || user.role == role
            matchesQuery && matchesRole
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Stores filters
    val storeSearchQuery = MutableStateFlow("")
    val storeCategoryFilter = MutableStateFlow<String?>(null)
    val filteredStores: StateFlow<List<Store>> = combine(
        allStores,
        storeSearchQuery,
        storeCategoryFilter
    ) { stores, query, category ->
        stores.filter { store ->
            val matchesQuery = query.isBlank() ||
                store.nameEn.contains(query, ignoreCase = true) ||
                store.nameAr.contains(query, ignoreCase = true) ||
                store.ownerName.contains(query, ignoreCase = true)
            val matchesCategory = category == null || store.categoryName.equals(category, ignoreCase = true)
            matchesQuery && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Drivers filters
    val driverSearchQuery = MutableStateFlow("")
    val driverAvailabilityFilter = MutableStateFlow<DriverAvailability?>(null)
    val filteredDrivers: StateFlow<List<Driver>> = combine(
        allDrivers,
        driverSearchQuery,
        driverAvailabilityFilter
    ) { drivers, query, availability ->
        drivers.filter { driver ->
            val matchesQuery = query.isBlank() ||
                driver.name.contains(query, ignoreCase = true) ||
                driver.phone.contains(query, ignoreCase = true)
            val matchesAvailability = availability == null || driver.availability == availability
            matchesQuery && matchesAvailability
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Products filters
    val productSearchQuery = MutableStateFlow("")
    val productStoreFilter = MutableStateFlow<String?>(null)
    val filteredProducts: StateFlow<List<Product>> = combine(
        allProducts,
        productSearchQuery,
        productStoreFilter
    ) { products, query, storeId ->
        products.filter { product ->
            val matchesQuery = query.isBlank() ||
                product.nameEn.contains(query, ignoreCase = true) ||
                product.nameAr.contains(query, ignoreCase = true) ||
                product.storeName.contains(query, ignoreCase = true)
            val matchesStore = storeId == null || product.storeId == storeId
            matchesQuery && matchesStore
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Banner / feedback message
    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    init {
        refreshDashboardMetrics()
    }

    fun toggleLanguage() {
        _isArabic.value = !_isArabic.value
    }

    fun navigateTo(screen: AdminScreen) {
        if (_currentScreen.value != screen) {
            screenBackStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        return if (screenBackStack.isNotEmpty()) {
            _currentScreen.value = screenBackStack.removeAt(screenBackStack.size - 1)
            true
        } else {
            false
        }
    }

    fun updateLoginEmail(email: String) {
        _loginEmail.value = email
        _loginError.value = null
    }

    fun updateLoginPassword(password: String) {
        _loginPassword.value = password
        _loginError.value = null
    }

    fun loginWithSupabase(userId: String, email: String) {
        repository.acceptSupabaseAdmin(userId, email)
        screenBackStack.clear()
        _currentScreen.value = AdminScreen.DASHBOARD
        _loginError.value = null
        viewModelScope.launch {
            val loaded = repository.refreshFromSupabase()
            if (loaded.isFailure) {
                _loginError.value = loaded.exceptionOrNull()?.message ?: "تعذر تحميل بيانات الإدارة من Supabase."
            }
            refreshDashboardMetrics()
        }
    }

    fun login() {
        _isAuthenticating.value = true
        _loginError.value = null
        val result = repository.authenticate(_loginEmail.value, _loginPassword.value)
        _isAuthenticating.value = false
        result.fold(
            onSuccess = {
                refreshDashboardMetrics()
                _currentScreen.value = AdminScreen.DASHBOARD
                showFeedback("Welcome, Administrator. Session authorized.")
            },
            onFailure = { error ->
                _loginError.value = error.message ?: "Authentication failed"
            }
        )
    }

    fun logout() {
        repository.logout()
        screenBackStack.clear()
        _currentScreen.value = AdminScreen.LOGIN
        _loginPassword.value = ""
        showFeedback("Logged out securely")
    }

    fun refreshDashboardMetrics() {
        viewModelScope.launch {
            _metrics.value = repository.getDashboardMetrics()
        }
    }

    fun selectOrderForDetails(order: Order) {
        selectedOrder.value = order
        navigateTo(AdminScreen.ORDER_DETAILS)
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus, driverId: String? = null) {
        viewModelScope.launch {
            val updated = repository.updateOrderStatus(orderId, newStatus, driverId)
            if (updated != null) {
                if (selectedOrder.value?.id == orderId) {
                    selectedOrder.value = updated
                }
                refreshDashboardMetrics()
                showFeedback("Order ${updated.orderNumber} updated to ${newStatus.name}")
            }
        }
    }

    fun assignDriverToOrder(orderId: String, driverId: String) {
        viewModelScope.launch {
            val updated = repository.assignDriver(orderId, driverId)
            if (updated != null) {
                if (selectedOrder.value?.id == orderId) {
                    selectedOrder.value = updated
                }
                refreshDashboardMetrics()
                showFeedback("Driver assigned to Order ${updated.orderNumber}")
            }
        }
    }

    fun toggleUserStatus(userId: String) {
        viewModelScope.launch {
            val updated = repository.toggleUserStatus(userId)
            if (updated != null) {
                showFeedback("User ${updated.name} status updated to ${updated.status}")
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            val updated = repository.updateUser(user)
            showFeedback("User ${updated.name} updated successfully")
        }
    }

    fun toggleStoreStatus(storeId: String) {
        viewModelScope.launch {
            val updated = repository.toggleStoreStatus(storeId)
            if (updated != null) {
                refreshDashboardMetrics()
                showFeedback("Store ${updated.nameEn} status: ${if (updated.isActive) "Active" else "Inactive"}")
            }
        }
    }

    fun toggleStoreOpen(storeId: String) {
        viewModelScope.launch {
            val updated = repository.toggleStoreOpen(storeId)
            if (updated != null) {
                refreshDashboardMetrics()
                showFeedback("Store ${updated.nameEn} is now ${if (updated.isOpen) "Open" else "Closed"}")
            }
        }
    }

    fun updateStore(store: Store) {
        viewModelScope.launch {
            val updated = repository.updateStore(store)
            showFeedback("Store ${updated.nameEn} updated")
        }
    }

    fun toggleDriverStatus(driverId: String) {
        viewModelScope.launch {
            val updated = repository.toggleDriverStatus(driverId)
            if (updated != null) {
                refreshDashboardMetrics()
                showFeedback("Driver ${updated.name} status: ${if (updated.isActive) "Active" else "Inactive"}")
            }
        }
    }

    fun setDriverAvailability(driverId: String, availability: DriverAvailability) {
        viewModelScope.launch {
            val updated = repository.setDriverAvailability(driverId, availability)
            if (updated != null) {
                refreshDashboardMetrics()
                showFeedback("Driver ${updated.name} availability: ${availability.name}")
            }
        }
    }

    fun createCategory(nameEn: String, nameAr: String, iconName: String) {
        viewModelScope.launch {
            val newCat = repository.createCategory(nameEn, nameAr, iconName)
            showFeedback("Category '${newCat.nameEn}' created")
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            val updated = repository.updateCategory(category)
            showFeedback("Category '${updated.nameEn}' updated")
        }
    }

    fun deleteCategory(categoryId: String) {
        viewModelScope.launch {
            val success = repository.deleteCategory(categoryId)
            if (success) {
                showFeedback("Category deleted successfully")
            }
        }
    }

    fun toggleProductAvailability(productId: String) {
        viewModelScope.launch {
            val updated = repository.toggleProductAvailability(productId)
            if (updated != null) {
                showFeedback("${updated.nameEn} availability: ${if (updated.isAvailable) "In Stock" else "Out of Stock"}")
            }
        }
    }

    fun toggleProductActive(productId: String) {
        viewModelScope.launch {
            val updated = repository.toggleProductActive(productId)
            if (updated != null) {
                showFeedback("${updated.nameEn} status: ${if (updated.isActive) "Active" else "Inactive"}")
            }
        }
    }

    fun updateSettings(settings: SystemSettings) {
        viewModelScope.launch {
            repository.updateSettings(settings)
            showFeedback("System platform settings saved")
        }
    }

    fun showFeedback(message: String) {
        _feedbackMessage.value = message
    }

    fun dismissFeedback() {
        _feedbackMessage.value = null
    }
}
