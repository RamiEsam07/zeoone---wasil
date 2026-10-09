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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.model.Category
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
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.StatusSuccessDark
import com.example.ui.util.AdminStrings
import com.example.ui.viewmodel.AdminViewModel

@Composable
fun CategoriesScreen(viewModel: AdminViewModel) {
    val isArabic by viewModel.isArabic.collectAsState()
    val categories by viewModel.allCategories.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<Category?>(null) }
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }

    // Create Category Dialog
    if (showCreateDialog) {
        var newNameEn by remember { mutableStateOf("") }
        var newNameAr by remember { mutableStateOf("") }
        var newIcon by remember { mutableStateOf("restaurant") }

        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = {
                Text(
                    text = AdminStrings.tr(isArabic, "Create Category", "إنشاء تصنيف جديد"),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = newNameEn,
                        onValueChange = { newNameEn = it },
                        label = { Text("Category Name (EN)") },
                        placeholder = { Text("e.g. Restaurants, Grocery") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("cat_create_name_en"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newNameAr,
                        onValueChange = { newNameAr = it },
                        label = { Text("اسم التصنيف (عربي)") },
                        placeholder = { Text("مثال: مطاعم، بقالة") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("cat_create_name_ar"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newIcon,
                        onValueChange = { newIcon = it },
                        label = { Text("Icon Tag") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newNameEn.isNotBlank() && newNameAr.isNotBlank()) {
                            viewModel.createCategory(newNameEn.trim(), newNameAr.trim(), newIcon.trim())
                            showCreateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                    modifier = Modifier.testTag("cat_create_submit")
                ) {
                    Text(AdminStrings.tr(isArabic, "Create", "إنشاء"))
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text(AdminStrings.tr(isArabic, "Cancel", "إلغاء"))
                }
            }
        )
    }

    // Edit Category Dialog
    if (categoryToEdit != null) {
        var editNameEn by remember { mutableStateOf(categoryToEdit!!.nameEn) }
        var editNameAr by remember { mutableStateOf(categoryToEdit!!.nameAr) }
        var editIcon by remember { mutableStateOf(categoryToEdit!!.iconName) }

        AlertDialog(
            onDismissRequest = { categoryToEdit = null },
            title = {
                Text(
                    text = AdminStrings.tr(isArabic, "Edit Category", "تعديل التصنيف"),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = editNameEn,
                        onValueChange = { editNameEn = it },
                        label = { Text("Category Name (EN)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = editNameAr,
                        onValueChange = { editNameAr = it },
                        label = { Text("اسم التصنيف (عربي)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updated = categoryToEdit!!.copy(nameEn = editNameEn.trim(), nameAr = editNameAr.trim())
                        viewModel.updateCategory(updated)
                        categoryToEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
                ) {
                    Text(AdminStrings.tr(isArabic, "Save", "حفظ"))
                }
            },
            dismissButton = {
                TextButton(onClick = { categoryToEdit = null }) {
                    Text(AdminStrings.tr(isArabic, "Cancel", "إلغاء"))
                }
            }
        )
    }

    // Delete Category Confirmation
    if (categoryToDelete != null) {
        val cat = categoryToDelete!!
        ConfirmDialog(
            title = AdminStrings.tr(isArabic, "Delete Category?", "حذف التصنيف؟"),
            message = AdminStrings.tr(
                isArabic,
                "Are you sure you want to delete '${if (isArabic) cat.nameAr else cat.nameEn}'? Stores associated with it may require reassignment.",
                "هل أنت متأكد من رغبتك في حذف تصنيف '${if (isArabic) cat.nameAr else cat.nameEn}'؟"
            ),
            confirmText = AdminStrings.tr(isArabic, "Delete Category", "حذف التصنيف"),
            cancelText = AdminStrings.tr(isArabic, "Cancel", "إلغاء"),
            isDestructive = true,
            onConfirm = {
                viewModel.deleteCategory(cat.id)
                categoryToDelete = null
            },
            onDismiss = { categoryToDelete = null }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top banner with Create button
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                        Text(
                            text = AdminStrings.tr(isArabic, "Category Taxonomy (CRUD)", "تصنيفات المنصة (إدارة كاملة)"),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = AdminStrings.tr(
                                isArabic,
                                "Configure business verticals, food and non-food catalog sectors",
                                "تهيئة القطاعات التجارية والغذائية وفئات المتاجر"
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate400
                        )
                    }

                    Button(
                        onClick = { showCreateDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("create_category_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(AdminStrings.tr(isArabic, "New Category", "تصنيف جديد"), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Categories list
        items(categories.size) { index ->
            val cat = categories[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("category_card_${cat.id}"),
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Category, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isArabic) "${cat.nameAr} (${cat.nameEn})" else "${cat.nameEn} (${cat.nameAr})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Storefront, contentDescription = null, tint = Slate400, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${cat.storeCount} ${AdminStrings.tr(isArabic, "Stores", "متاجر")}",
                                    fontSize = 12.sp,
                                    color = Slate600
                                )
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { categoryToEdit = cat },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("edit_cat_${cat.id}")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AdminStrings.tr(isArabic, "Edit", "تعديل"), fontSize = 11.sp)
                        }

                        Button(
                            onClick = { categoryToDelete = cat },
                            colors = ButtonDefaults.buttonColors(containerColor = StatusDanger),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("delete_cat_${cat.id}")
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AdminStrings.tr(isArabic, "Delete", "حذف"), fontSize = 11.sp)
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
