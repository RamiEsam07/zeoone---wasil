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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import com.example.data.model.User
import com.example.data.model.UserRole
import com.example.data.model.UserStatus
import com.example.ui.components.ConfirmDialog
import com.example.ui.components.UserRoleBadge
import com.example.ui.components.UserStatusBadge
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
import com.example.ui.theme.StatusSuccess
import com.example.ui.util.AdminStrings
import com.example.ui.viewmodel.AdminViewModel

@Composable
fun UsersScreen(viewModel: AdminViewModel) {
    val isArabic by viewModel.isArabic.collectAsState()
    val users by viewModel.filteredUsers.collectAsState()
    val searchQuery by viewModel.userSearchQuery.collectAsState()
    val selectedRole by viewModel.userRoleFilter.collectAsState()

    var userPendingToggle by remember { mutableStateOf<User?>(null) }
    var userPendingEdit by remember { mutableStateOf<User?>(null) }

    // Confirmation dialog for toggle status
    if (userPendingToggle != null) {
        val user = userPendingToggle!!
        val isActivating = user.status == UserStatus.INACTIVE
        ConfirmDialog(
            title = if (isActivating) {
                AdminStrings.tr(isArabic, "Activate Account?", "تفعيل الحساب؟")
            } else {
                AdminStrings.tr(isArabic, "Deactivate Account?", "تعطيل الحساب؟")
            },
            message = AdminStrings.tr(
                isArabic,
                "Are you sure you want to ${if (isActivating) "activate" else "deactivate"} '${user.name}' (${user.role.name})?",
                "هل أنت متأكد من رغبتك في ${if (isActivating) "تفعيل" else "تعطيل"} حساب '${user.name}' (${AdminStrings.roleLabel(user.role, true)})؟"
            ),
            confirmText = if (isActivating) {
                AdminStrings.tr(isArabic, "Activate", "تفعيل")
            } else {
                AdminStrings.tr(isArabic, "Deactivate", "تعطيل")
            },
            isDestructive = !isActivating,
            onConfirm = {
                viewModel.toggleUserStatus(user.id)
                userPendingToggle = null
            },
            onDismiss = { userPendingToggle = null }
        )
    }

    // Edit User Dialog
    if (userPendingEdit != null) {
        var editName by remember { mutableStateOf(userPendingEdit!!.name) }
        var editPhone by remember { mutableStateOf(userPendingEdit!!.phone) }

        AlertDialog(
            onDismissRequest = { userPendingEdit = null },
            title = {
                Text(
                    text = AdminStrings.tr(isArabic, "Edit User Information", "تعديل بيانات المستخدم"),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "ID: ${userPendingEdit!!.id} • ${userPendingEdit!!.email}",
                        fontSize = 12.sp,
                        color = Slate400
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text(AdminStrings.tr(isArabic, "Full Name", "الاسم الكامل")) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text(AdminStrings.tr(isArabic, "Phone Number", "رقم الهاتف")) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updated = userPendingEdit!!.copy(name = editName.trim(), phone = editPhone.trim())
                        viewModel.updateUser(updated)
                        userPendingEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
                ) {
                    Text(AdminStrings.tr(isArabic, "Save Changes", "حفظ التغييرات"))
                }
            },
            dismissButton = {
                TextButton(onClick = { userPendingEdit = null }) {
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
                        text = AdminStrings.tr(isArabic, "Users Directory & Role Governance", "دليل المستخدمين وإدارة الأدوار"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AdminStrings.tr(
                            isArabic,
                            "Manage Customers, Stores, Drivers, and Platform Administrators",
                            "إدارة حسابات العملاء، المتاجر، السائقين، ومسؤولي النظام"
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.userSearchQuery.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("users_search_input"),
                        placeholder = {
                            Text(
                                text = AdminStrings.tr(isArabic, "Search by name, email, or phone...", "ابحث بالاسم، البريد أو الهاتف..."),
                                fontSize = 13.sp,
                                color = Slate400
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Slate400)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { viewModel.userSearchQuery.value = "" }) {
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

                    // Roles tab row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedRole == null,
                            onClick = { viewModel.userRoleFilter.value = null },
                            label = { Text(AdminStrings.tr(isArabic, "All Roles", "جميع الأدوار"), fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandBlueContainer,
                                selectedLabelColor = BrandBlueDark
                            ),
                            modifier = Modifier.testTag("filter_role_all")
                        )

                        UserRole.entries.forEach { role ->
                            FilterChip(
                                selected = selectedRole == role,
                                onClick = {
                                    viewModel.userRoleFilter.value = if (selectedRole == role) null else role
                                },
                                label = {
                                    Text(
                                        text = AdminStrings.roleLabel(role, isArabic),
                                        fontSize = 12.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandBlueContainer,
                                    selectedLabelColor = BrandBlueDark
                                ),
                                modifier = Modifier.testTag("filter_role_${role.name}")
                            )
                        }
                    }
                }
            }
        }

        // Counter
        item {
            Text(
                text = "${users.size} ${AdminStrings.tr(isArabic, "Users Registered", "مستخدم مسجل")}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Slate700
            )
        }

        // User list
        items(users.size) { index ->
            val user = users[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("user_row_${user.id}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate200))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = user.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                UserRoleBadge(role = user.role, isArabic = isArabic)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = user.email, fontSize = 12.sp, color = Slate600)
                        }

                        UserStatusBadge(status = user.status, isArabic = isArabic)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${AdminStrings.tr(isArabic, "Phone", "الهاتف")}: ${user.phone}",
                                fontSize = 12.sp,
                                color = Slate700
                            )
                            Text(
                                text = "${AdminStrings.tr(isArabic, "Joined", "تاريخ التسجيل")}: ${user.createdAt}",
                                fontSize = 11.sp,
                                color = Slate400
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Edit
                            OutlinedButton(
                                onClick = { userPendingEdit = user },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("edit_user_button_${user.id}")
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(AdminStrings.tr(isArabic, "Edit", "تعديل"), fontSize = 11.sp)
                            }

                            // Activate / Deactivate
                            Button(
                                onClick = { userPendingToggle = user },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (user.status == UserStatus.ACTIVE) StatusDanger else StatusSuccess
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("toggle_user_status_${user.id}")
                            ) {
                                Text(
                                    text = if (user.status == UserStatus.ACTIVE) {
                                        AdminStrings.tr(isArabic, "Deactivate", "تعطيل")
                                    } else {
                                        AdminStrings.tr(isArabic, "Activate", "تفعيل")
                                    },
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
