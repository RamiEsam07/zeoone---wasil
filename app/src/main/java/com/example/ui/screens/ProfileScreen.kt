package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.example.BuildConfig
import com.example.ui.components.StatusBadge
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
import com.example.ui.theme.StatusDangerContainer
import com.example.ui.theme.StatusDangerDark
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.StatusSuccessDark
import com.example.ui.util.AdminStrings
import com.example.ui.viewmodel.AdminViewModel

@Composable
fun ProfileScreen(viewModel: AdminViewModel) {
    val isArabic by viewModel.isArabic.collectAsState()
    val admin = viewModel.currentAdmin.collectAsState().value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Admin Profile Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_profile_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(BrandBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = admin?.name ?: "Administrator",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = admin?.email ?: "admin@deliveryplatform.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate600
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    StatusBadge(
                        text = "ROLE: SUPER_ADMINISTRATOR",
                        containerColor = BrandBlueContainer,
                        contentColor = BrandBlueDark
                    )
                }
            }
        }

        // Security & Secrets Governance Status
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AdminStrings.tr(isArabic, "Security & Environment Secrets Status", "حالة الأمان ومتغيرات البيئة"),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileDetailRow(
                        icon = Icons.Default.Key,
                        title = AdminStrings.tr(isArabic, "Credential Source", "مصدر بيانات الاعتماد"),
                        value = "BuildConfig / Secrets Gradle Plugin (.env)"
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileDetailRow(
                        icon = Icons.Default.CloudDone,
                        title = AdminStrings.tr(isArabic, "Backend API Gateway", "بوابة الخادم والواجهة البرمجية"),
                        value = try { BuildConfig.ADMIN_API_BASE_URL } catch (_: Exception) { "https://api.deliveryplatform.com" }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileDetailRow(
                        icon = Icons.Default.Security,
                        title = AdminStrings.tr(isArabic, "Active Session Token", "رمز الجلسة الموثقة"),
                        value = admin?.token?.take(18) + "..."
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileDetailRow(
                        icon = Icons.Default.CheckCircle,
                        title = AdminStrings.tr(isArabic, "Role-Based Access Control", "التحكم في الصلاحيات (RBAC)"),
                        value = AdminStrings.tr(isArabic, "Active (Admin Only)", "نشط ومفعل (صلاحيات المدير)")
                    )
                }
            }
        }

        // Permissions Matrix
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AdminStrings.tr(isArabic, "Granted Administrative Scopes", "الصلاحيات الإدارية الممنوحة"),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val scopes = listOf(
                        "ORDERS:VIEW_MUTATE",
                        "USERS:MANAGE_ROLES",
                        "STORES:ACTIVATE_GOVERN",
                        "DRIVERS:DISPATCH_MONITOR",
                        "CATALOG:CATEGORIES_PRODUCTS",
                        "SYSTEM:COMMISSIONS_DISPATCH"
                    )

                    scopes.forEach { sc ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = StatusSuccess,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = sc,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate700
                            )
                        }
                    }
                }
            }
        }

        // Logout Button
        item {
            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("admin_profile_logout_button"),
                colors = ButtonDefaults.buttonColors(containerColor = StatusDanger),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AdminStrings.tr(isArabic, "Terminate Session & Logout", "إنهاء الجلسة وتسجيل الخروج"),
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

@Composable
private fun ProfileDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = Slate600, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title, fontSize = 12.sp, color = Slate600)
        }
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Slate900
        )
    }
}
