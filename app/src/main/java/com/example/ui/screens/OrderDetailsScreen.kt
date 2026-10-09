package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderStatus
import com.example.ui.components.ConfirmDialog
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
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningDark
import com.example.ui.util.AdminStrings
import com.example.ui.viewmodel.AdminScreen
import com.example.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(viewModel: AdminViewModel) {
    val isArabic by viewModel.isArabic.collectAsState()
    val order = viewModel.selectedOrder.collectAsState().value
    val drivers by viewModel.allDrivers.collectAsState()

    var showCancelDialog by remember { mutableStateOf(false) }
    var showAssignDriverDropdown by remember { mutableStateOf(false) }

    if (order == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = AdminStrings.tr(isArabic, "No order selected", "لم يتم تحديد أي طلب"),
                color = Slate400
            )
        }
        return
    }

    if (showCancelDialog) {
        ConfirmDialog(
            title = AdminStrings.tr(isArabic, "Cancel Order ${order.orderNumber}?", "إلغاء الطلب ${order.orderNumber}؟"),
            message = AdminStrings.tr(
                isArabic,
                "Are you sure you want to cancel this order? This will terminate delivery and notify the store and customer.",
                "هل أنت متأكد من رغبتك في إلغاء هذا الطلب؟ سيتم إيقاف عملية التوصيل وإخطار المتجر والعميل."
            ),
            confirmText = AdminStrings.tr(isArabic, "Yes, Cancel Order", "نعم، قم بالإلغاء"),
            cancelText = AdminStrings.tr(isArabic, "Keep Order", "تراجع"),
            isDestructive = true,
            onConfirm = {
                viewModel.updateOrderStatus(order.id, OrderStatus.CANCELLED)
                showCancelDialog = false
            },
            onDismiss = { showCancelDialog = false }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Back Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(AdminScreen.ORDERS) },
                    modifier = Modifier.testTag("back_to_orders_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Slate700
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = order.orderNumber,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        OrderStatusBadge(status = order.status, isArabic = isArabic)
                    }
                    Text(
                        text = "${AdminStrings.tr(isArabic, "Created", "تاريخ الإنشاء")}: ${order.createdAt} • ${AdminStrings.tr(isArabic, "Updated", "آخر تحديث")}: ${order.updatedAt}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )
                }
            }
        }

        // Shared Lifecycle Stepper
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AdminStrings.tr(isArabic, "Delivery Platform Lifecycle", "دورة حياة الطلب الموحدة"),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    val standardFlow = listOf(
                        OrderStatus.NEW,
                        OrderStatus.ACCEPTED,
                        OrderStatus.PREPARING,
                        OrderStatus.READY,
                        OrderStatus.PICKED_UP,
                        OrderStatus.ON_THE_WAY,
                        OrderStatus.DELIVERED
                    )

                    val currentIndex = standardFlow.indexOf(order.status)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        standardFlow.forEachIndexed { idx, st ->
                            val isPassed = currentIndex >= idx && currentIndex != -1
                            val isCurrent = order.status == st

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isCurrent) BrandBlue
                                            else if (isPassed) StatusSuccess
                                            else Slate200
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isPassed && !isCurrent) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "${idx + 1}",
                                            color = if (isCurrent) Color.White else Slate600,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = AdminStrings.orderStatusLabel(st, isArabic),
                                    fontSize = 10.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCurrent) BrandBlueDark else Slate600
                                )
                            }

                            if (idx < standardFlow.size - 1) {
                                Box(
                                    modifier = Modifier
                                        .width(20.dp)
                                        .height(2.dp)
                                        .background(if (currentIndex > idx) StatusSuccess else Slate200)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Admin Lifecycle Transition Actions
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AdminStrings.tr(isArabic, "Admin Lifecycle Transition Control", "التحكم الإداري في مراحل الطلب"),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AdminStrings.tr(
                            isArabic,
                            "Override or progress order status through platform delivery stages",
                            "تقديم حالة الطلب أو تعديلها يدوياً عبر مراحل المنصة"
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        when (order.status) {
                            OrderStatus.NEW -> {
                                Button(
                                    onClick = { viewModel.updateOrderStatus(order.id, OrderStatus.ACCEPTED) },
                                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(AdminStrings.tr(isArabic, "Accept Order", "قبول الطلب"))
                                }
                                OutlinedButton(
                                    onClick = { viewModel.updateOrderStatus(order.id, OrderStatus.REJECTED) },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(AdminStrings.tr(isArabic, "Reject", "رفض الطلب"), color = StatusDanger)
                                }
                            }
                            OrderStatus.ACCEPTED -> {
                                Button(
                                    onClick = { viewModel.updateOrderStatus(order.id, OrderStatus.PREPARING) },
                                    colors = ButtonDefaults.buttonColors(containerColor = StatusWarning),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(AdminStrings.tr(isArabic, "Set Preparing", "بدء التحضير"))
                                }
                            }
                            OrderStatus.PREPARING -> {
                                Button(
                                    onClick = { viewModel.updateOrderStatus(order.id, OrderStatus.READY) },
                                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(AdminStrings.tr(isArabic, "Mark Ready for Pickup", "جاهز للاستلام"))
                                }
                            }
                            OrderStatus.READY -> {
                                Button(
                                    onClick = { viewModel.updateOrderStatus(order.id, OrderStatus.PICKED_UP) },
                                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(AdminStrings.tr(isArabic, "Driver Picked Up", "استلم السائق"))
                                }
                            }
                            OrderStatus.PICKED_UP -> {
                                Button(
                                    onClick = { viewModel.updateOrderStatus(order.id, OrderStatus.ON_THE_WAY) },
                                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(AdminStrings.tr(isArabic, "Driver On The Way", "في الطريق للعميل"))
                                }
                            }
                            OrderStatus.ON_THE_WAY -> {
                                Button(
                                    onClick = { viewModel.updateOrderStatus(order.id, OrderStatus.DELIVERED) },
                                    colors = ButtonDefaults.buttonColors(containerColor = StatusSuccess),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(AdminStrings.tr(isArabic, "Mark Delivered", "تم التوصيل بنجاح"))
                                }
                            }
                            OrderStatus.DELIVERED, OrderStatus.CANCELLED, OrderStatus.REJECTED -> {
                                Text(
                                    text = AdminStrings.tr(isArabic, "This order is in a completed terminal state.", "هذا الطلب وصل إلى حالته النهائية."),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate600
                                )
                            }
                        }

                        if (!order.status.isTerminal()) {
                            OutlinedButton(
                                onClick = { showCancelDialog = true },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("admin_cancel_order_button")
                            ) {
                                Text(AdminStrings.tr(isArabic, "Emergency Cancel", "إلغاء طارئ"), color = StatusDanger)
                            }
                        }
                    }
                }
            }
        }

        // Entities Involved (Customer, Store, Driver)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AdminStrings.tr(isArabic, "Participants & Routing", "الأطراف المعنية ومسار التوصيل"),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Customer Row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${AdminStrings.tr(isArabic, "Customer", "العميل")}: ${order.customerName}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Slate900
                            )
                            Text(text = order.customerPhone, fontSize = 12.sp, color = Slate600)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Store Row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Storefront, contentDescription = null, tint = StatusWarning, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${AdminStrings.tr(isArabic, "Store", "المتجر")}: ${order.storeName}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Slate900
                            )
                            Text(text = "ID: ${order.storeId}", fontSize = 12.sp, color = Slate400)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Driver Row & Assignment Dropdown
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DirectionsBike, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "${AdminStrings.tr(isArabic, "Driver", "السائق")}: ${order.driverName ?: AdminStrings.tr(isArabic, "Unassigned", "غير معين")}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (order.driverName != null) Slate900 else StatusWarningDark
                                )
                                Text(
                                    text = if (order.driverId != null) "ID: ${order.driverId}" else AdminStrings.tr(isArabic, "Awaiting dispatch", "بانتظار الإسناد"),
                                    fontSize = 12.sp,
                                    color = Slate400
                                )
                            }
                        }

                        if (!order.status.isTerminal()) {
                            ExposedDropdownMenuBox(
                                expanded = showAssignDriverDropdown,
                                onExpandedChange = { showAssignDriverDropdown = it }
                            ) {
                                OutlinedButton(
                                    onClick = { showAssignDriverDropdown = true },
                                    modifier = Modifier.menuAnchor(),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = AdminStrings.tr(isArabic, "Assign Driver", "تعيين سائق"),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                ExposedDropdownMenu(
                                    expanded = showAssignDriverDropdown,
                                    onDismissRequest = { showAssignDriverDropdown = false }
                                ) {
                                    drivers.forEach { drv ->
                                        DropdownMenuItem(
                                            text = {
                                                Column {
                                                    Text(text = drv.name, fontWeight = FontWeight.SemiBold)
                                                    Text(
                                                        text = "${drv.availability.name} • ${drv.vehicleType}",
                                                        fontSize = 11.sp,
                                                        color = Slate400
                                                    )
                                                }
                                            },
                                            onClick = {
                                                viewModel.assignDriverToOrder(order.id, drv.id)
                                                showAssignDriverDropdown = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Address Row
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AdminStrings.tr(isArabic, "Delivery Destination Address", "عنوان التوصيل"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Slate900
                            )
                            Text(text = order.address, fontSize = 12.sp, color = Slate600)
                            if (order.notes.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${AdminStrings.tr(isArabic, "Customer Instructions", "ملاحظات العميل")}: \"${order.notes}\"",
                                    fontSize = 11.sp,
                                    color = Slate500
                                )
                            }
                        }
                    }
                }
            }
        }

        // Itemized Products & Financial Breakdown
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AdminStrings.tr(isArabic, "Products & Invoice Breakdown", "المنتجات وتفاصيل الفاتورة"),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    order.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isArabic) item.productNameAr else item.productNameEn,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = Slate900
                                )
                                Text(
                                    text = "${item.quantity} x $${String.format("%.2f", item.unitPrice)}",
                                    fontSize = 12.sp,
                                    color = Slate400
                                )
                            }
                            Text(
                                text = "$${String.format("%.2f", item.totalPrice)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Slate900
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Subtotal
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = AdminStrings.tr(isArabic, "Subtotal", "المجموع الفرعي"), fontSize = 13.sp, color = Slate600)
                        Text(text = "$${String.format("%.2f", order.subtotal)}", fontSize = 13.sp, color = Slate700)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Delivery Fee
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = AdminStrings.tr(isArabic, "Delivery Fee", "رسوم التوصيل"), fontSize = 13.sp, color = Slate600)
                        Text(text = "$${String.format("%.2f", order.deliveryFee)}", fontSize = 13.sp, color = Slate700)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Total
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AdminStrings.tr(isArabic, "Grand Total", "الإجمالي النهائي"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Slate900
                        )
                        Text(
                            text = "$${String.format("%.2f", order.total)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = BrandBlueDark
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
private val Slate500 = Color(0xFF64748B)
