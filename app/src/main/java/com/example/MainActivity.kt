package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AdminBottomBar
import com.example.ui.components.AdminHorizontalNavStrip
import com.example.ui.components.AdminTopBar
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DriversScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.OrderDetailsScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProductsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StoresScreen
import com.example.ui.screens.UsersScreen
import com.example.ui.theme.BrandBlueDark
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate900
import com.example.ui.util.AdminStrings
import com.example.ui.viewmodel.AdminScreen
import com.example.ui.viewmodel.AdminViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AdminAppRoot()
            }
        }
    }
}

@Composable
fun AdminAppRoot(viewModel: AdminViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isArabic by viewModel.isArabic.collectAsState()
    val adminUser by viewModel.currentAdmin.collectAsState()
    val feedbackMessage by viewModel.feedbackMessage.collectAsState()

    // Back button handling
    BackHandler(enabled = currentScreen != AdminScreen.LOGIN && currentScreen != AdminScreen.DASHBOARD) {
        if (!viewModel.navigateBack()) {
            viewModel.navigateTo(AdminScreen.DASHBOARD)
        }
    }

    // Dynamic layout direction for full Arabic RTL support
    val layoutDirection = if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        if (currentScreen == AdminScreen.LOGIN) {
            LoginScreen(viewModel = viewModel)
        } else {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("admin_main_scaffold"),
                topBar = {
                    val title = when (currentScreen) {
                        AdminScreen.DASHBOARD -> AdminStrings.tr(isArabic, "Dashboard", "لوحة المؤشرات")
                        AdminScreen.ORDERS -> AdminStrings.tr(isArabic, "Orders", "إدارة الطلبات")
                        AdminScreen.ORDER_DETAILS -> AdminStrings.tr(isArabic, "Order Details", "تفاصيل الطلب")
                        AdminScreen.USERS -> AdminStrings.tr(isArabic, "Users", "إدارة المستخدمين")
                        AdminScreen.STORES -> AdminStrings.tr(isArabic, "Stores", "إدارة المتاجر")
                        AdminScreen.DRIVERS -> AdminStrings.tr(isArabic, "Drivers", "إدارة السائقين")
                        AdminScreen.CATEGORIES -> AdminStrings.tr(isArabic, "Categories", "التصنيفات")
                        AdminScreen.PRODUCTS -> AdminStrings.tr(isArabic, "Products", "كتالوج المنتجات")
                        AdminScreen.SETTINGS -> AdminStrings.tr(isArabic, "System Settings", "إعدادات النظام")
                        AdminScreen.PROFILE -> AdminStrings.tr(isArabic, "Admin Profile", "الملف الإداري")
                        else -> "Admin"
                    }

                    AdminTopBar(
                        title = title,
                        adminName = adminUser?.name,
                        isArabic = isArabic,
                        onToggleLanguage = { viewModel.toggleLanguage() },
                        onRefresh = { viewModel.refreshDashboardMetrics() },
                        onLogout = { viewModel.logout() }
                    )
                },
                bottomBar = {
                    AdminBottomBar(
                        currentScreen = currentScreen,
                        isArabic = isArabic,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Quick analytical sub-nav strip
                        AdminHorizontalNavStrip(
                            currentScreen = currentScreen,
                            isArabic = isArabic,
                            onNavigate = { viewModel.navigateTo(it) }
                        )

                        // Main screen content area
                        Box(modifier = Modifier.weight(1f)) {
                            when (currentScreen) {
                                AdminScreen.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                                AdminScreen.ORDERS -> OrdersScreen(viewModel = viewModel)
                                AdminScreen.ORDER_DETAILS -> OrderDetailsScreen(viewModel = viewModel)
                                AdminScreen.USERS -> UsersScreen(viewModel = viewModel)
                                AdminScreen.STORES -> StoresScreen(viewModel = viewModel)
                                AdminScreen.DRIVERS -> DriversScreen(viewModel = viewModel)
                                AdminScreen.CATEGORIES -> CategoriesScreen(viewModel = viewModel)
                                AdminScreen.PRODUCTS -> ProductsScreen(viewModel = viewModel)
                                AdminScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
                                AdminScreen.PROFILE -> ProfileScreen(viewModel = viewModel)
                                else -> DashboardScreen(viewModel = viewModel)
                            }
                        }
                    }

                    // Floating feedback toast notification
                    AnimatedVisibility(
                        visible = feedbackMessage != null,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    ) {
                        feedbackMessage?.let { msg ->
                            LaunchedEffect(msg) {
                                delay(3000)
                                viewModel.dismissFeedback()
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Slate900)
                                    .border(1.dp, Slate200.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                                    .testTag("admin_feedback_toast")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = msg,
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
