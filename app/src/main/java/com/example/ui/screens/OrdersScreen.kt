package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderStatus
import com.example.ui.components.OrderStatusBadge
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandBlueContainer
import com.example.ui.theme.BrandBlueDark
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.util.AdminStrings
import com.example.ui.viewmodel.AdminViewModel

@Composable
fun OrdersScreen(viewModel: AdminViewModel) {
    val isArabic by viewModel.isArabic.collectAsState()
    val orders by viewModel.filteredOrders.collectAsState()
    val searchQuery by viewModel.orderSearchQuery.collectAsState()
    val selectedStatus by viewModel.orderStatusFilter.collectAsState()
    val stores by viewModel.allStores.collectAsState()
    val drivers by viewModel.allDrivers.collectAsState()
    val selectedStore by viewModel.orderStoreFilter.collectAsState()
    val selectedDriver by viewModel.orderDriverFilter.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Search & Filters Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AdminStrings.tr(isArabic, "Order Management Center", "إدارة الطلبات والعمليات"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AdminStrings.tr(
                            isArabic,
                            "Search, filter and inspect orders across customers, stores, and drivers",
                            "البحث والتصفية ومراقبة دورة حياة الطلبات بين المتاجر والسائقين والعملاء"
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Search input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.orderSearchQuery.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("orders_search_input"),
                        placeholder = {
                            Text(
                                text = AdminStrings.tr(
                                    isArabic,
                                    "Search by order #, customer, store, driver...",
                                    "ابحث برقم الطلب، العميل، المتجر، أو السائق..."
                                ),
                                fontSize = 13.sp,
                                color = Slate400
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Slate400)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { viewModel.orderSearchQuery.value = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Slate400)
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandBlue,
                            unfocusedBorderColor = Slate200
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Status Filters Strip
                    Text(
                        text = AdminStrings.tr(isArabic, "Filter by Status:", "تصفية حسب الحالة:"),
                        style = MaterialTheme.typography.labelSmall,
                        color = Slate600,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = selectedStatus == null,
                            onClick = { viewModel.orderStatusFilter.value = null },
                            label = { Text(AdminStrings.tr(isArabic, "All Statuses", "كل الحالات"), fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandBlueContainer,
                                selectedLabelColor = BrandBlueDark
                            ),
                            modifier = Modifier.testTag("filter_status_all")
                        )

                        OrderStatus.entries.forEach { status ->
                            FilterChip(
                                selected = selectedStatus == status,
                                onClick = {
                                    viewModel.orderStatusFilter.value = if (selectedStatus == status) null else status
                                },
                                label = {
                                    Text(
                                        text = AdminStrings.orderStatusLabel(status, isArabic),
                                        fontSize = 12.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandBlueContainer,
                                    selectedLabelColor = BrandBlueDark
                                ),
                                modifier = Modifier.testTag("filter_status_${status.name}")
                            )
                        }
                    }

                    // Store / Driver filters row
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = AdminStrings.tr(isArabic, "Filter by Store:", "تصفية حسب المتجر:"),
                        style = MaterialTheme.typography.labelSmall,
                        color = Slate600,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = selectedStore == null,
                            onClick = { viewModel.orderStoreFilter.value = null },
                            label = { Text(AdminStrings.tr(isArabic, "All Stores", "كل المتاجر"), fontSize = 12.sp) }
                        )
                        stores.forEach { store ->
                            FilterChip(
                                selected = selectedStore == store.id,
                                onClick = {
                                    viewModel.orderStoreFilter.value = if (selectedStore == store.id) null else store.id
                                },
                                label = { Text(if (isArabic) store.nameAr else store.nameEn, fontSize = 12.sp) }
                            )
                        }
                    }
                }
            }
        }

        // Results counter
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${orders.size} ${AdminStrings.tr(isArabic, "Orders Found", "طلب تم العثور عليه")}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Slate700
                )

                if (selectedStatus != null || selectedStore != null || searchQuery.isNotBlank()) {
                    Text(
                        text = AdminStrings.tr(isArabic, "Reset Filters", "إعادة ضبط الفلاتر"),
                        style = MaterialTheme.typography.labelSmall,
                        color = BrandBlue,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable {
                                viewModel.orderSearchQuery.value = ""
                                viewModel.orderStatusFilter.value = null
                                viewModel.orderStoreFilter.value = null
                                viewModel.orderDriverFilter.value = null
                            }
                            .testTag("reset_order_filters_button")
                    )
                }
            }
        }

        // Orders List
        if (orders.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = null,
                                tint = Slate400,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = AdminStrings.tr(isArabic, "No orders matching your criteria", "لا توجد طلبات تطابق معايير البحث المحددة"),
                                color = Slate600,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        } else {
            items(orders.size) { index ->
                val order = orders[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectOrderForDetails(order) }
                        .testTag("order_item_row_${order.orderNumber}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = order.orderNumber,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                OrderStatusBadge(status = order.status, isArabic = isArabic)
                            }

                            Text(
                                text = "$${String.format("%.2f", order.total)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandBlueDark
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Customer & Store info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${AdminStrings.tr(isArabic, "Store", "المتجر")}: ${order.storeName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Slate700
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${AdminStrings.tr(isArabic, "Customer", "العميل")}: ${order.customerName} (${order.customerPhone})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate600
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = order.createdAt,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Slate400
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${order.items.size} ${AdminStrings.tr(isArabic, "items", "عناصر")}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Slate600
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Driver tag and Address
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${AdminStrings.tr(isArabic, "Driver", "السائق")}: ${order.driverName ?: AdminStrings.tr(isArabic, "Not Assigned", "لم يعين بعد")}",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (order.driverName != null) Slate700 else Slate400,
                                fontWeight = if (order.driverName != null) FontWeight.Medium else FontWeight.Normal
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { viewModel.selectOrderForDetails(order) }
                            ) {
                                Text(
                                    text = AdminStrings.tr(isArabic, "Details", "التفاصيل"),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandBlue
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Details",
                                    tint = BrandBlue,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
