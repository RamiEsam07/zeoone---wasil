package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.util.AdminStrings
import com.example.ui.viewmodel.AdminViewModel

@Composable
fun SettingsScreen(viewModel: AdminViewModel) {
    val isArabic by viewModel.isArabic.collectAsState()
    val currentSettings by viewModel.settings.collectAsState()

    var commissionRate by remember(currentSettings) { mutableStateOf(currentSettings.commissionRatePercent.toString()) }
    var baseFee by remember(currentSettings) { mutableStateOf(currentSettings.baseDeliveryFee.toString()) }
    var feePerKm by remember(currentSettings) { mutableStateOf(currentSettings.feePerKm.toString()) }
    var taxRate by remember(currentSettings) { mutableStateOf(currentSettings.taxRatePercent.toString()) }
    var autoAssign by remember(currentSettings) { mutableStateOf(currentSettings.autoAssignDrivers) }
    var maxRadius by remember(currentSettings) { mutableStateOf(currentSettings.maxDeliveryRadiusKm.toString()) }
    var maintenanceMode by remember(currentSettings) { mutableStateOf(currentSettings.maintenanceMode) }
    var currency by remember(currentSettings) { mutableStateOf(currentSettings.currencySymbol) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AdminStrings.tr(isArabic, "Platform System Settings", "إعدادات منصة التوصيل المركزية"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AdminStrings.tr(
                            isArabic,
                            "Global delivery fee algorithms, commission cuts, dispatch automation, and language",
                            "تحديد نسب العمولات، تعرفة التوصيل، الأتمتة واللغة الافتراضية"
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )
                }
            }
        }

        // Financial & Fee Configurations
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AdminStrings.tr(isArabic, "Financial & Pricing Parameters", "العمولات والرسوم المالية"),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Commission Rate
                    OutlinedTextField(
                        value = commissionRate,
                        onValueChange = { commissionRate = it },
                        label = { Text(AdminStrings.tr(isArabic, "Platform Commission (%)", "عمولة المنصة على المتاجر (%)")) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_commission_rate"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Base Delivery Fee
                    OutlinedTextField(
                        value = baseFee,
                        onValueChange = { baseFee = it },
                        label = { Text(AdminStrings.tr(isArabic, "Base Delivery Fee ($)", "رسوم التوصيل الأساسية ($)")) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_base_fee"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Fee Per Km
                    OutlinedTextField(
                        value = feePerKm,
                        onValueChange = { feePerKm = it },
                        label = { Text(AdminStrings.tr(isArabic, "Distance Fee Per Km ($)", "رسوم المسافة لكل كيلومتر ($)")) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tax Rate
                    OutlinedTextField(
                        value = taxRate,
                        onValueChange = { taxRate = it },
                        label = { Text(AdminStrings.tr(isArabic, "Value Added Tax / Tax Rate (%)", "ضريبة القيمة المضافة (%)")) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                }
            }
        }

        // Logistics & Dispatch Rules
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AdminStrings.tr(isArabic, "Dispatch & Operational Governance", "قواعد الإسناد والتشغيل"),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Auto Assign Drivers Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AdminStrings.tr(isArabic, "Smart Dispatch Algorithm", "الإسناد التلقائي الذكي للسائقين"),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = Slate900
                            )
                            Text(
                                text = AdminStrings.tr(
                                    isArabic,
                                    "Automatically assign nearest AVAILABLE driver upon order acceptance",
                                    "تعيين أقرب سائق متاح فور قبول المتجر للطلب"
                                ),
                                fontSize = 11.sp,
                                color = Slate600
                            )
                        }
                        Switch(
                            checked = autoAssign,
                            onCheckedChange = { autoAssign = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = BrandBlue,
                                checkedTrackColor = BrandBlueContainer
                            ),
                            modifier = Modifier.testTag("settings_auto_assign_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Max delivery radius
                    OutlinedTextField(
                        value = maxRadius,
                        onValueChange = { maxRadius = it },
                        label = { Text(AdminStrings.tr(isArabic, "Maximum Delivery Radius (km)", "أقصى نطاق للتوصيل (كم)")) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Platform Maintenance Mode
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AdminStrings.tr(isArabic, "Platform Maintenance Mode", "وضع صيانة المنصة"),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = if (maintenanceMode) StatusDanger else Slate900
                            )
                            Text(
                                text = AdminStrings.tr(
                                    isArabic,
                                    "Temporarily disables customer checkout across the delivery apps",
                                    "إيقاف إنشاء طلبات جديدة مؤقتاً لأعمال التحديث"
                                ),
                                fontSize = 11.sp,
                                color = Slate600
                            )
                        }
                        Switch(
                            checked = maintenanceMode,
                            onCheckedChange = { maintenanceMode = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = StatusDanger,
                                checkedTrackColor = StatusDangerContainer
                            ),
                            modifier = Modifier.testTag("settings_maintenance_switch")
                        )
                    }
                }
            }
        }

        // Language & Localization Settings
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AdminStrings.tr(isArabic, "Localization & Display Options", "خيارات اللغة والعرض"),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AdminStrings.tr(isArabic, "Arabic RTL Interface", "واجهة عربية باتجاه اليمين (RTL)"),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = Slate900
                            )
                            Text(
                                text = AdminStrings.tr(
                                    isArabic,
                                    "Toggle right-to-left layout and Arabic localized vocabulary",
                                    "تفعيل التخطيط العربي من اليمين لليسار وترجمة جميع المصطلحات"
                                ),
                                fontSize = 11.sp,
                                color = Slate600
                            )
                        }
                        Switch(
                            checked = isArabic,
                            onCheckedChange = { viewModel.toggleLanguage() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = BrandBlue,
                                checkedTrackColor = BrandBlueContainer
                            ),
                            modifier = Modifier.testTag("settings_rtl_switch")
                        )
                    }
                }
            }
        }

        // Save Action
        item {
            Button(
                onClick = {
                    val updated = currentSettings.copy(
                        commissionRatePercent = commissionRate.toDoubleOrNull() ?: currentSettings.commissionRatePercent,
                        baseDeliveryFee = baseFee.toDoubleOrNull() ?: currentSettings.baseDeliveryFee,
                        feePerKm = feePerKm.toDoubleOrNull() ?: currentSettings.feePerKm,
                        taxRatePercent = taxRate.toDoubleOrNull() ?: currentSettings.taxRatePercent,
                        autoAssignDrivers = autoAssign,
                        maxDeliveryRadiusKm = maxRadius.toDoubleOrNull() ?: currentSettings.maxDeliveryRadiusKm,
                        maintenanceMode = maintenanceMode,
                        currencySymbol = currency
                    )
                    viewModel.updateSettings(updated)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_settings_button"),
                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AdminStrings.tr(isArabic, "Save Platform Settings", "حفظ إعدادات المنصة"),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
