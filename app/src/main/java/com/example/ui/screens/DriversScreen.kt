package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Driver
import com.example.data.model.DriverAvailability
import com.example.ui.components.ConfirmDialog
import com.example.ui.components.DriverAvailabilityBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandBlueContainer
import com.example.ui.theme.BrandBlueDark
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.StatusDangerContainer
import com.example.ui.theme.StatusDangerDark
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.StatusSuccessDark
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningContainer
import com.example.ui.theme.StatusWarningDark
import com.example.ui.util.AdminStrings
import com.example.ui.viewmodel.AdminViewModel

@Composable
fun DriversScreen(viewModel: AdminViewModel) {
    val isArabic by viewModel.isArabic.collectAsState()
    val drivers by viewModel.filteredDrivers.collectAsState()
    val searchQuery by viewModel.driverSearchQuery.collectAsState()
    val selectedAvailability by viewModel.driverAvailabilityFilter.collectAsState()

    var driverPendingToggle by remember { mutableStateOf<Driver?>(null) }

    if (driverPendingToggle != null) {
        val driver = driverPendingToggle!!
        val isActivating = !driver.isActive
        ConfirmDialog(
            title = if (isActivating) AdminStrings.tr(isArabic, "Activate Driver?", "تفعيل السائق؟") else AdminStrings.tr(isArabic, "Deactivate Driver?", "تعطيل السائق؟"),
            message = AdminStrings.tr(
                isArabic,
                "Are you sure you want to ${if (isActivating) "activate" else "deactivate"} driver '${driver.name}'?",
                "هل أنت متأكد من رغبتك في ${if (isActivating) "تفعيل" else "تعطيل"} حساب السائق '${driver.name}'؟"
            ),
            confirmText = if (isActivating) AdminStrings.tr(isArabic, "Activate", "تفعيل") else AdminStrings.tr(isArabic, "Deactivate", "تعطيل"),
            isDestructive = !isActivating,
            onConfirm = {
                viewModel.toggleDriverStatus(driver.id)
                driverPendingToggle = null
            },
            onDismiss = { driverPendingToggle = null }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Controls header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AdminStrings.tr(isArabic, "Driver Fleet Dispatch & Operations", "إدارة أسطول السائقين والمناديب"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AdminStrings.tr(
                            isArabic,
                            "Monitor real-time fleet availability, ongoing runs, and completion performance",
                            "متابعة توفر الأسطول الفعلي، الرحلات الجارية، وسجل التوصيل"
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.driverSearchQuery.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("drivers_search_input"),
                        placeholder = {
                            Text(
                                text = AdminStrings.tr(isArabic, "Search by driver name or phone...", "ابحث باسم السائق أو رقم الهاتف..."),
                                fontSize = 13.sp,
                                color = Slate400
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Slate400)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { viewModel.driverSearchQuery.value = "" }) {
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

                    // Availability chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedAvailability == null,
                            onClick = { viewModel.driverAvailabilityFilter.value = null },
                            label = { Text(AdminStrings.tr(isArabic, "All Availabilities", "جميع الحالات"), fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandBlueContainer,
                                selectedLabelColor = BrandBlueDark
                            ),
                            modifier = Modifier.testTag("filter_avail_all")
                        )

                        DriverAvailability.entries.forEach { avail ->
                            FilterChip(
                                selected = selectedAvailability == avail,
                                onClick = {
                                    viewModel.driverAvailabilityFilter.value = if (selectedAvailability == avail) null else avail
                                },
                                label = {
                                    Text(
                                        text = AdminStrings.driverAvailabilityLabel(avail, isArabic),
                                        fontSize = 12.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandBlueContainer,
                                    selectedLabelColor = BrandBlueDark
                                ),
                                modifier = Modifier.testTag("filter_avail_${avail.name}")
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "${drivers.size} ${AdminStrings.tr(isArabic, "Drivers Registered", "سائق مسجل")}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Slate700
            )
        }

        // Drivers List
        items(drivers.size) { index ->
            val driver = drivers[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("driver_card_${driver.id}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DirectionsBike, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = driver.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Text(
                                    text = "${driver.vehicleType} • ${driver.phone}",
                                    fontSize = 12.sp,
                                    color = Slate600
                                )
                            }
                        }

                        DriverAvailabilityBadge(avail = driver.availability, isArabic = isArabic)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Deliveries & Current order info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${driver.completedDeliveries} ${AdminStrings.tr(isArabic, "Deliveries Completed", "توصيلة مكتملة")}",
                                fontSize = 12.sp,
                                color = Slate700,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = StatusWarning, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${driver.rating}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate700)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${AdminStrings.tr(isArabic, "Current Delivery", "الطلب الحالي")}: ${driver.currentOrderId ?: AdminStrings.tr(isArabic, "None (Idle)", "لا يوجد (في الانتظار)")}",
                        fontSize = 12.sp,
                        color = if (driver.currentOrderId != null) BrandBlueDark else Slate400,
                        fontWeight = if (driver.currentOrderId != null) FontWeight.Bold else FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Admin Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Quick status override
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            OutlinedButton(
                                onClick = { viewModel.setDriverAvailability(driver.id, DriverAvailability.AVAILABLE) },
                                shape = RoundedCornerShape(6.dp),
                                enabled = driver.availability != DriverAvailability.AVAILABLE,
                                modifier = Modifier.testTag("driver_set_avail_${driver.id}")
                            ) {
                                Text(AdminStrings.tr(isArabic, "Available", "متاح"), fontSize = 10.sp)
                            }
                            OutlinedButton(
                                onClick = { viewModel.setDriverAvailability(driver.id, DriverAvailability.OFFLINE) },
                                shape = RoundedCornerShape(6.dp),
                                enabled = driver.availability != DriverAvailability.OFFLINE,
                                modifier = Modifier.testTag("driver_set_offline_${driver.id}")
                            ) {
                                Text(AdminStrings.tr(isArabic, "Offline", "غير متصل"), fontSize = 10.sp)
                            }
                        }

                        Button(
                            onClick = { driverPendingToggle = driver },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (driver.isActive) StatusDanger else StatusSuccess
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("toggle_driver_active_${driver.id}")
                        ) {
                            Text(
                                text = if (driver.isActive) AdminStrings.tr(isArabic, "Deactivate", "تعطيل") else AdminStrings.tr(isArabic, "Activate", "تفعيل"),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
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
