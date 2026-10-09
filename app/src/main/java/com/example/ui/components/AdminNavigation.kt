package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandBlueContainer
import com.example.ui.theme.BrandBlueDark
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.util.AdminStrings
import com.example.ui.viewmodel.AdminScreen

data class NavItem(
    val screen: AdminScreen,
    val titleEn: String,
    val titleAr: String,
    val icon: ImageVector
)

val adminNavItems = listOf(
    NavItem(AdminScreen.DASHBOARD, "Dashboard", "لوحة المؤشرات", Icons.Default.Dashboard),
    NavItem(AdminScreen.ORDERS, "Orders", "الطلبات", Icons.Default.ReceiptLong),
    NavItem(AdminScreen.USERS, "Users", "المستخدمين", Icons.Default.People),
    NavItem(AdminScreen.STORES, "Stores", "المتاجر", Icons.Default.Storefront),
    NavItem(AdminScreen.DRIVERS, "Drivers", "السائقين", Icons.Default.DirectionsBike),
    NavItem(AdminScreen.CATEGORIES, "Categories", "التصنيفات", Icons.Default.Category),
    NavItem(AdminScreen.PRODUCTS, "Products", "المنتجات", Icons.Default.Inventory2),
    NavItem(AdminScreen.SETTINGS, "Settings", "الإعدادات", Icons.Default.Settings),
    NavItem(AdminScreen.PROFILE, "Profile", "الملف الشخصي", Icons.Default.Person)
)

@Composable
fun AdminSidebarRail(
    currentScreen: AdminScreen,
    isArabic: Boolean,
    onNavigate: (AdminScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier
            .fillMaxHeight()
            .border(1.dp, Slate200)
            .background(Color.White),
        containerColor = Color.White,
        header = {
            Spacer(modifier = Modifier.height(16.dp))
        }
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            adminNavItems.forEach { item ->
                val selected = currentScreen == item.screen
                NavigationRailItem(
                    selected = selected,
                    onClick = { onNavigate(item.screen) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = AdminStrings.tr(isArabic, item.titleEn, item.titleAr)
                        )
                    },
                    label = {
                        Text(
                            text = AdminStrings.tr(isArabic, item.titleEn, item.titleAr),
                            fontSize = 10.sp,
                            maxLines = 1
                        )
                    },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = BrandBlueDark,
                        selectedTextColor = BrandBlueDark,
                        indicatorColor = BrandBlueContainer,
                        unselectedIconColor = Slate600,
                        unselectedTextColor = Slate600
                    ),
                    modifier = Modifier.testTag("nav_item_${item.screen.name.lowercase()}")
                )
            }
        }
    }
}

@Composable
fun AdminBottomBar(
    currentScreen: AdminScreen,
    isArabic: Boolean,
    onNavigate: (AdminScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    // Top-tier items for bottom bar
    val mainBottomItems = listOf(
        adminNavItems[0], // Dashboard
        adminNavItems[1], // Orders
        adminNavItems[2], // Users
        adminNavItems[3], // Stores
        adminNavItems[4]  // Drivers
    )

    NavigationBar(
        modifier = modifier.border(1.dp, Slate200),
        containerColor = Color.White,
        tonalElevation = 2.dp
    ) {
        mainBottomItems.forEach { item ->
            val selected = currentScreen == item.screen
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = AdminStrings.tr(isArabic, item.titleEn, item.titleAr)
                    )
                },
                label = {
                    Text(
                        text = AdminStrings.tr(isArabic, item.titleEn, item.titleAr),
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BrandBlueDark,
                    selectedTextColor = BrandBlueDark,
                    indicatorColor = BrandBlueContainer,
                    unselectedIconColor = Slate600,
                    unselectedTextColor = Slate600
                ),
                modifier = Modifier.testTag("bottom_nav_${item.screen.name.lowercase()}")
            )
        }
    }
}

@Composable
fun AdminHorizontalNavStrip(
    currentScreen: AdminScreen,
    isArabic: Boolean,
    onNavigate: (AdminScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, Slate200)
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        adminNavItems.forEach { item ->
            val isSelected = currentScreen == item.screen
            val label = AdminStrings.tr(isArabic, item.titleEn, item.titleAr)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) BrandBlueContainer else Slate100)
                    .border(1.dp, if (isSelected) BrandBlue else Slate200, RoundedCornerShape(8.dp))
                    .clickable { onNavigate(item.screen) }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .testTag("nav_pill_${item.screen.name.lowercase()}"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = label,
                        tint = if (isSelected) BrandBlueDark else Slate700,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) BrandBlueDark else Slate700,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
private val Slate100 = Color(0xFFF1F5F9)
