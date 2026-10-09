package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.components.MetricCard
import com.example.ui.components.OrderStatusBadge
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
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningContainer
import com.example.ui.theme.StatusWarningDark
import com.example.ui.util.AdminStrings
import com.example.ui.viewmodel.AdminScreen
import com.example.ui.viewmodel.AdminViewModel

@Composable
fun DashboardScreen(viewModel: AdminViewModel) {
    val isArabic by viewModel.isArabic.collectAsState()
    val metrics by viewModel.metrics.collectAsState()
    val orders by viewModel.allOrders.collectAsState()
    val stores by viewModel.allStores.collectAsState()
    val drivers by viewModel.allDrivers.collectAsState()

    val activeOrdersList = orders.filter { it.status.isActiveOrder() }
    val unassignedOrders = activeOrdersList.filter { it.driverId == null }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Command Center Operational Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_header_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(StatusSuccess)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AdminStrings.tr(isArabic, "PLATFORM STATUS: ALL SYSTEMS ONLINE", "حالة المنصة: جميع الأنظمة تعمل بكفاءة"),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = StatusSuccess
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = AdminStrings.tr(isArabic, "Operations Command Center", "مركز قيادة العمليات اللوجستية"),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(BrandBlueContainer)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${activeOrdersList.size} ${AdminStrings.tr(isArabic, "Live Orders", "طلبات قيد التنفيذ")}",
                            color = BrandBlueDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Action Attention Alert (if unassigned or pending orders exist)
        if (unassignedOrders.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("attention_alert_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = StatusWarningContainer),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StatusWarning.copy(alpha = 0.5f)))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = StatusWarningDark,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AdminStrings.tr(isArabic, "Attention Required", "تنبيه يتطلب المتابعة"),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = StatusWarningDark
                            )
                            Text(
                                text = AdminStrings.tr(
                                    isArabic,
                                    "${unassignedOrders.size} active order(s) are awaiting driver assignment.",
                                    "يوجد ${unassignedOrders.size} طلب نشط بانتظار تعيين سائق للتوصيل."
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = StatusWarningDark
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White)
                                .clickable { viewModel.navigateTo(AdminScreen.ORDERS) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = AdminStrings.tr(isArabic, "View", "عرض"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = StatusWarningDark
                            )
                        }
                    }
                }
            }
        }

        // Section: Primary Order Metrics (Row 1)
        item {
            Text(
                text = AdminStrings.tr(isArabic, "Order Metrics", "مؤشرات الطلبات"),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Slate700
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = AdminStrings.tr(isArabic, "Total Orders", "إجمالي الطلبات"),
                    value = "${metrics?.totalOrders ?: 0}",
                    icon = Icons.Default.ReceiptLong,
                    accentColor = BrandBlue,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = AdminStrings.tr(isArabic, "Today's Orders", "طلبات اليوم"),
                    value = "${metrics?.todayOrders ?: 0}",
                    icon = Icons.Default.Today,
                    accentColor = BrandBlueDark,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = AdminStrings.tr(isArabic, "Active Orders", "الطلبات النشطة"),
                    value = "${metrics?.activeOrders ?: 0}",
                    icon = Icons.Default.LocalShipping,
                    accentColor = StatusWarning,
                    subtext = AdminStrings.tr(isArabic, "In delivery pipeline", "قيد المعالجة والتوصيل"),
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = AdminStrings.tr(isArabic, "Delivered", "الطلبات المكتملة"),
                    value = "${metrics?.deliveredOrders ?: 0}",
                    icon = Icons.Default.CheckCircle,
                    accentColor = StatusSuccess,
                    subtext = AdminStrings.tr(isArabic, "Successfully completed", "تم تسليمها بنجاح"),
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = AdminStrings.tr(isArabic, "Cancelled", "الطلبات الملغاة"),
                    value = "${metrics?.cancelledOrders ?: 0}",
                    icon = Icons.Default.Cancel,
                    accentColor = StatusDanger,
                    subtext = AdminStrings.tr(isArabic, "Failed/Cancelled", "ملغية أو مرفوضة"),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Section: Network Capacity & Partners (Row 2)
        item {
            Text(
                text = AdminStrings.tr(isArabic, "Stores & Drivers Capacity", "طاقة المتاجر والسائقين"),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Slate700
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = AdminStrings.tr(isArabic, "Total Stores", "إجمالي المتاجر"),
                    value = "${metrics?.totalStores ?: 0}",
                    icon = Icons.Default.Storefront,
                    accentColor = BrandBlue,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = AdminStrings.tr(isArabic, "Active Stores", "المتاجر المفتوحة"),
                    value = "${metrics?.activeStores ?: 0}",
                    icon = Icons.Default.Storefront,
                    accentColor = StatusSuccess,
                    subtext = AdminStrings.tr(isArabic, "Open for orders", "تستقبل الطلبات حالياً"),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = AdminStrings.tr(isArabic, "Total Drivers", "إجمالي السائقين"),
                    value = "${metrics?.totalDrivers ?: 0}",
                    icon = Icons.Default.DirectionsBike,
                    accentColor = BrandBlueDark,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = AdminStrings.tr(isArabic, "Available Drivers", "السائقين المتاحين"),
                    value = "${metrics?.availableDrivers ?: 0}",
                    icon = Icons.Default.DirectionsBike,
                    accentColor = StatusSuccess,
                    subtext = AdminStrings.tr(isArabic, "Ready for dispatch", "جاهزون لاستلام مهام"),
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = AdminStrings.tr(isArabic, "Customers", "العملاء المسجلين"),
                    value = "${metrics?.customers ?: 0}",
                    icon = Icons.Default.People,
                    accentColor = Slate700,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Financial summary card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = AdminStrings.tr(isArabic, "Delivered Gross Merchandise Value", "إجمالي قيمة الطلبات المسلمة"),
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate600
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$${String.format("%.2f", metrics?.totalRevenue ?: 0.0)}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(StatusSuccess.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = null,
                            tint = StatusSuccess,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // Order Pipeline Distribution Bar
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AdminStrings.tr(isArabic, "Order Pipeline Distribution", "توزيع تدفق الطلبات"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = AdminStrings.tr(
                            isArabic,
                            "Live state representation across all registered delivery cycles",
                            "تمثيل حي لحالات الطلبات عبر مراحل دورة التوصيل"
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val total = orders.size.coerceAtLeast(1).toFloat()
                    val deliveredRatio = (metrics?.deliveredOrders ?: 0) / total
                    val activeRatio = (metrics?.activeOrders ?: 0) / total
                    val cancelledRatio = (metrics?.cancelledOrders ?: 0) / total

                    LinearProgressIndicator(
                        progress = { deliveredRatio },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = StatusSuccess,
                        trackColor = Slate200
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PipelineLegendItem(color = StatusSuccess, label = AdminStrings.tr(isArabic, "Delivered", "مكتمل"), count = metrics?.deliveredOrders ?: 0)
                        PipelineLegendItem(color = StatusWarning, label = AdminStrings.tr(isArabic, "In Progress", "قيد التنفيذ"), count = metrics?.activeOrders ?: 0)
                        PipelineLegendItem(color = StatusDanger, label = AdminStrings.tr(isArabic, "Cancelled", "ملغي"), count = metrics?.cancelledOrders ?: 0)
                    }
                }
            }
        }

        // Active Orders Live Ticker
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AdminStrings.tr(isArabic, "Active Orders In Progress", "الطلبات الجارية حالياً"),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Text(
                    text = AdminStrings.tr(isArabic, "View All →", "عرض الكل ←"),
                    style = MaterialTheme.typography.labelMedium,
                    color = BrandBlue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { viewModel.navigateTo(AdminScreen.ORDERS) }
                )
            }
        }

        if (activeOrdersList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = AdminStrings.tr(isArabic, "No orders currently active.", "لا توجد طلبات جارية حالياً."),
                        color = Slate400,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            items(activeOrdersList.size) { index ->
                val order = activeOrdersList[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectOrderForDetails(order) }
                        .testTag("active_order_card_${order.orderNumber}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = order.orderNumber,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                OrderStatusBadge(status = order.status, isArabic = isArabic)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${order.storeName} • ${order.customerName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate600
                            )
                            Text(
                                text = "${AdminStrings.tr(isArabic, "Driver", "السائق")}: ${order.driverName ?: AdminStrings.tr(isArabic, "Unassigned", "غير معين")}",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (order.driverName != null) Slate700 else StatusWarningDark,
                                fontWeight = if (order.driverName == null) FontWeight.Bold else FontWeight.Normal
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "$${String.format("%.2f", order.total)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "View Details",
                                tint = Slate400,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun PipelineLegendItem(color: Color, label: String, count: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$label: $count",
            style = MaterialTheme.typography.bodySmall,
            color = Slate600,
            fontSize = 12.sp
        )
    }
}
