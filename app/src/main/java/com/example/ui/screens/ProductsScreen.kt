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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.util.AdminStrings
import com.example.ui.viewmodel.AdminViewModel

@Composable
fun ProductsScreen(viewModel: AdminViewModel) {
    val isArabic by viewModel.isArabic.collectAsState()
    val products by viewModel.filteredProducts.collectAsState()
    val stores by viewModel.allStores.collectAsState()
    val searchQuery by viewModel.productSearchQuery.collectAsState()
    val selectedStoreId by viewModel.productStoreFilter.collectAsState()

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
                        text = AdminStrings.tr(isArabic, "Cross-Store Catalog Monitoring", "مراقبة الكتالوج والمنتجات عبر المتاجر"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AdminStrings.tr(
                            isArabic,
                            "Preserving strict store ownership boundaries while tracking availability and pricing",
                            "الحفاظ على ملكية المتاجر مع مراقبة الأسعار وتوفر الأصناف على مستوى المنصة"
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.productSearchQuery.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("products_search_input"),
                        placeholder = {
                            Text(
                                text = AdminStrings.tr(isArabic, "Search by product name or store...", "ابحث باسم المنتج أو المتجر..."),
                                fontSize = 13.sp,
                                color = Slate400
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Slate400)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { viewModel.productSearchQuery.value = "" }) {
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

                    // Store filters
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedStoreId == null,
                            onClick = { viewModel.productStoreFilter.value = null },
                            label = { Text(AdminStrings.tr(isArabic, "All Stores", "جميع المتاجر"), fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandBlueContainer,
                                selectedLabelColor = BrandBlueDark
                            ),
                            modifier = Modifier.testTag("filter_prod_store_all")
                        )

                        stores.forEach { store ->
                            FilterChip(
                                selected = selectedStoreId == store.id,
                                onClick = {
                                    viewModel.productStoreFilter.value = if (selectedStoreId == store.id) null else store.id
                                },
                                label = { Text(if (isArabic) store.nameAr else store.nameEn, fontSize = 12.sp) },
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
                text = "${products.size} ${AdminStrings.tr(isArabic, "Products Monitored", "منتج مراقب")}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Slate700
            )
        }

        // Products list
        items(products.size) { index ->
            val product = products[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("product_card_${product.id}"),
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
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Default.Inventory2, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isArabic) product.nameAr else product.nameEn,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = product.description,
                                    fontSize = 11.sp,
                                    color = Slate600
                                )
                            }
                        }

                        Text(
                            text = "$${String.format("%.2f", product.price)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = BrandBlueDark
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Store Ownership Badge & Category
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Storefront, contentDescription = null, tint = Slate600, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${AdminStrings.tr(isArabic, "Owned by", "المتجر المالك")}: ${product.storeName}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Slate700
                            )
                        }

                        StatusBadge(
                            text = product.categoryName,
                            containerColor = Slate100,
                            contentColor = Slate700
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Availability and Active toggles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Stock availability toggle
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (product.isAvailable) AdminStrings.tr(isArabic, "In Stock", "متوفر للطلب") else AdminStrings.tr(isArabic, "Out of Stock", "نفد المخزون"),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (product.isAvailable) StatusSuccessDark else StatusDangerDark
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Switch(
                                checked = product.isAvailable,
                                onCheckedChange = { viewModel.toggleProductAvailability(product.id) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = StatusSuccess,
                                    checkedTrackColor = StatusSuccessContainer
                                ),
                                modifier = Modifier.testTag("toggle_prod_avail_${product.id}")
                            )
                        }

                        // Product status active toggle
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (product.isActive) AdminStrings.tr(isArabic, "Published", "منشور") else AdminStrings.tr(isArabic, "Hidden", "مخفي"),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (product.isActive) BrandBlueDark else Slate400
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Switch(
                                checked = product.isActive,
                                onCheckedChange = { viewModel.toggleProductActive(product.id) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = BrandBlue,
                                    checkedTrackColor = BrandBlueContainer
                                ),
                                modifier = Modifier.testTag("toggle_prod_active_${product.id}")
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
private val Slate100 = Color(0xFFF1F5F9)
