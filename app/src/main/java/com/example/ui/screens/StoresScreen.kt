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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.model.Store
import com.example.ui.components.ConfirmDialog
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
fun StoresScreen(viewModel: AdminViewModel) {
    val isArabic by viewModel.isArabic.collectAsState()
    val stores by viewModel.filteredStores.collectAsState()
    val categories by viewModel.allCategories.collectAsState()
    val searchQuery by viewModel.storeSearchQuery.collectAsState()
    val selectedCategory by viewModel.storeCategoryFilter.collectAsState()

    var storePendingToggle by remember { mutableStateOf<Store?>(null) }
    var storePendingEdit by remember { mutableStateOf<Store?>(null) }

    // Confirmation Dialog for Store Activate / Deactivate
    if (storePendingToggle != null) {
        val store = storePendingToggle!!
        val isActivating = !store.isActive
        ConfirmDialog(
            title = if (isActivating) {
                AdminStrings.tr(isArabic, "Activate Store?", "تفعيل المتجر؟")
            } else {
                AdminStrings.tr(isArabic, "Deactivate Store?", "تعطيل المتجر؟")
            },
            message = AdminStrings.tr(
                isArabic,
                "Are you sure you want to ${if (isActivating) "activate" else "deactivate"} '${if (isArabic) store.nameAr else store.nameEn}'? Deactivated stores cannot receive orders.",
                "هل أنت متأكد من رغبتك في ${if (isActivating) "تفعيل" else "تعطيل"} متجر '${if (isArabic) store.nameAr else store.nameEn}'؟ لن يتمكن المتجر المعطل من استقبال الطلبات."
            ),
            confirmText = if (isActivating) AdminStrings.tr(isArabic, "Activate", "تفعيل") else AdminStrings.tr(isArabic, "Deactivate", "تعطيل"),
            isDestructive = !isActivating,
            onConfirm = {
                viewModel.toggleStoreStatus(store.id)
                storePendingToggle = null
            },
            onDismiss = { storePendingToggle = null }
        )
    }

    // Edit Store Dialog
    if (storePendingEdit != null) {
        var editNameEn by remember { mutableStateOf(storePendingEdit!!.nameEn) }
        var editNameAr by remember { mutableStateOf(storePendingEdit!!.nameAr) }
        var editOwner by remember { mutableStateOf(storePendingEdit!!.ownerName) }
        var editPhone by remember { mutableStateOf(storePendingEdit!!.phone) }
        var editAddress by remember { mutableStateOf(storePendingEdit!!.address) }

        AlertDialog(
            onDismissRequest = { storePendingEdit = null },
            title = {
                Text(
                    text = AdminStrings.tr(isArabic, "Edit Store Details", "تعديل بيانات المتجر"),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = editNameEn,
                        onValueChange = { editNameEn = it },
                        label = { Text("Store Name (EN)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editNameAr,
                        onValueChange = { editNameAr = it },
                        label = { Text("اسم المتجر (عربي)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editOwner,
                        onValueChange = { editOwner = it },
                        label = { Text(AdminStrings.tr(isArabic, "Owner Name", "اسم المالك")) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text(AdminStrings.tr(isArabic, "Phone", "رقم الهاتف")) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editAddress,
                        onValueChange = { editAddress = it },
                        label = { Text(AdminStrings.tr(isArabic, "Address", "العنوان")) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updated = storePendingEdit!!.copy(
                            nameEn = editNameEn.trim(),
                            nameAr = editNameAr.trim(),
                            ownerName = editOwner.trim(),
                            phone = editPhone.trim(),
                            address = editAddress.trim()
                        )
                        viewModel.updateStore(updated)
                        storePendingEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
                ) {
                    Text(AdminStrings.tr(isArabic, "Save", "حفظ"))
                }
            },
            dismissButton = {
                TextButton(onClick = { storePendingEdit = null }) {
                    Text(AdminStrings.tr(isArabic, "Cancel", "إلغاء"))
                }
            }
        )
    }

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
                        text = AdminStrings.tr(isArabic, "Store Operations & Governance", "إدارة المتاجر والشركاء التجاريين"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AdminStrings.tr(
                            isArabic,
                            "Monitor merchant profiles, opening status, and active platform authorization",
                            "متابعة حالة فتح وإغلاق المتاجر، والصلاحيات التشغيلية للمنصة"
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.storeSearchQuery.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("stores_search_input"),
                        placeholder = {
                            Text(
                                text = AdminStrings.tr(isArabic, "Search by store name, owner, or address...", "ابحث باسم المتجر، المالك، أو العنوان..."),
                                fontSize = 13.sp,
                                color = Slate400
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Slate400)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { viewModel.storeSearchQuery.value = "" }) {
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

                    // Category filters
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { viewModel.storeCategoryFilter.value = null },
                            label = { Text(AdminStrings.tr(isArabic, "All Categories", "جميع التصنيفات"), fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandBlueContainer,
                                selectedLabelColor = BrandBlueDark
                            ),
                            modifier = Modifier.testTag("filter_store_cat_all")
                        )

                        categories.forEach { cat ->
                            FilterChip(
                                selected = selectedCategory == cat.nameEn,
                                onClick = {
                                    viewModel.storeCategoryFilter.value = if (selectedCategory == cat.nameEn) null else cat.nameEn
                                },
                                label = { Text(if (isArabic) cat.nameAr else cat.nameEn, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandBlueContainer,
                                    selectedLabelColor = BrandBlueDark
                                )
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "${stores.size} ${AdminStrings.tr(isArabic, "Stores Registered", "متجر مسجل")}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Slate700
            )
        }

        // Stores list
        items(stores.size) { index ->
            val store = stores[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("store_card_${store.id}"),
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
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isArabic) store.nameAr else store.nameEn,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                StatusBadge(
                                    text = store.categoryName,
                                    containerColor = BrandBlueContainer,
                                    contentColor = BrandBlueDark
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${AdminStrings.tr(isArabic, "Owner", "المالك")}: ${store.ownerName}",
                                fontSize = 12.sp,
                                color = Slate600
                            )
                        }

                        // Open / Closed Indicator & Rating
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = StatusWarning, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${store.rating}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Slate700
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatusBadge(
                                text = if (store.isOpen) AdminStrings.tr(isArabic, "OPEN", "مفتوح") else AdminStrings.tr(isArabic, "CLOSED", "مغلق"),
                                containerColor = if (store.isOpen) StatusSuccessContainer else StatusWarningContainer,
                                contentColor = if (store.isOpen) StatusSuccessDark else StatusWarningDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Contact & Address
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = Slate400, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = store.phone, fontSize = 12.sp, color = Slate600)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Slate400, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = store.address, fontSize = 12.sp, color = Slate600)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Admin Actions Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Open / Closed Quick Switch
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = AdminStrings.tr(isArabic, "Open Status:", "حالة الاستقبال:"),
                                fontSize = 11.sp,
                                color = Slate600
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Switch(
                                checked = store.isOpen,
                                onCheckedChange = { viewModel.toggleStoreOpen(store.id) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = StatusSuccess,
                                    checkedTrackColor = StatusSuccessContainer
                                ),
                                modifier = Modifier.testTag("toggle_store_open_${store.id}")
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { storePendingEdit = store },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("edit_store_${store.id}")
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(AdminStrings.tr(isArabic, "Edit", "تعديل"), fontSize = 11.sp)
                            }

                            Button(
                                onClick = { storePendingToggle = store },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (store.isActive) StatusDanger else StatusSuccess
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("toggle_store_active_${store.id}")
                            ) {
                                Text(
                                    text = if (store.isActive) AdminStrings.tr(isArabic, "Deactivate", "تعطيل") else AdminStrings.tr(isArabic, "Activate", "تفعيل"),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
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
