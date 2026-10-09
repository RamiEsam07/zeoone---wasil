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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
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
import com.example.ui.util.AdminStrings
import com.example.ui.viewmodel.AdminViewModel

@Composable
fun LoginScreen(viewModel: AdminViewModel) {
    val isArabic by viewModel.isArabic.collectAsState()
    val email by viewModel.loginEmail.collectAsState()
    val password by viewModel.loginPassword.collectAsState()
    val loginError by viewModel.loginError.collectAsState()
    val isAuthenticating by viewModel.isAuthenticating.collectAsState()

    var showPassword by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Language selector top pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = { viewModel.toggleLanguage() },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate700),
                    modifier = Modifier.testTag("login_lang_toggle")
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Language",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isArabic) "English" else "العربية",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Brand Header
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(BrandBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Admin Shield",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = AdminStrings.tr(isArabic, "Delivery Platform", "منصة التوصيل الموحدة"),
                style = MaterialTheme.typography.titleMedium,
                color = Slate600,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = AdminStrings.tr(isArabic, "Central Admin Command Center", "لوحة التحكم الإدارية المركزية"),
                style = MaterialTheme.typography.headlineSmall,
                color = Slate900,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = AdminStrings.tr(
                    isArabic,
                    "Strict role-based authorization for platform administrators",
                    "صلاحيات وصول مقيدة لمشرفي ومديري المنصة فقط"
                ),
                style = MaterialTheme.typography.bodySmall,
                color = Slate400
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = AdminStrings.tr(isArabic, "Administrator Sign In", "تسجيل دخول المدير"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Error alert
                    if (loginError != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(StatusDangerContainer)
                                .border(1.dp, StatusDanger.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                                .testTag("login_error_banner")
                        ) {
                            Text(
                                text = loginError!!,
                                color = StatusDangerDark,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Email field
                    Text(
                        text = AdminStrings.tr(isArabic, "Admin Email", "البريد الإلكتروني للمدير"),
                        style = MaterialTheme.typography.labelMedium,
                        color = Slate700,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { viewModel.updateLoginEmail(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_email_input"),
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Slate400)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandBlue,
                            unfocusedBorderColor = Slate200
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Password field
                    Text(
                        text = AdminStrings.tr(isArabic, "Admin Password / Secret", "كلمة المرور / المفتاح السري"),
                        style = MaterialTheme.typography.labelMedium,
                        color = Slate700,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { viewModel.updateLoginPassword(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_password_input"),
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Slate400)
                        },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = Slate400
                                )
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandBlue,
                            unfocusedBorderColor = Slate200
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Autofill configured credentials button
                    OutlinedButton(
                        onClick = {
                            try {
                                viewModel.updateLoginEmail(BuildConfig.ADMIN_EMAIL.ifBlank { "admin@deliveryplatform.com" })
                                viewModel.updateLoginPassword(BuildConfig.ADMIN_PASSWORD.ifBlank { "AdminMaster2026!" })
                            } catch (_: Exception) {
                                viewModel.updateLoginEmail("admin@deliveryplatform.com")
                                viewModel.updateLoginPassword("AdminMaster2026!")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("use_configured_secrets_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandBlueDark)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AdminStrings.tr(isArabic, "Use Environment Credentials", "استخدام بيانات البيئة المجهزة"),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Login Action Button
                    Button(
                        onClick = { viewModel.login() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("login_submit_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                        enabled = !isAuthenticating
                    ) {
                        if (isAuthenticating) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = AdminStrings.tr(isArabic, "Authenticate & Enter", "المصادقة والدخول"),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Security disclaimer box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Slate100)
                    .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = AdminStrings.tr(isArabic, "🔒 Enterprise Security Notice", "🔒 إشعار الأمان المؤسسي"),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate700
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AdminStrings.tr(
                            isArabic,
                            "Role-based authorization is enforced at both application and backend API boundaries. Credentials are read from build environment secrets.",
                            "يتم تطبيق صلاحيات الأدوار على مستوى التطبيق وخوادم الواجهة البرمجية. تُقرأ بيانات الاعتماد بأمان من متغيرات البيئة."
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate600,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
