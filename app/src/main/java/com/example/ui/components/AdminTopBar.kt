package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate900
import com.example.ui.util.AdminStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTopBar(
    title: String,
    adminName: String?,
    isArabic: Boolean,
    onToggleLanguage: () -> Unit,
    onRefresh: () -> Unit,
    onLogout: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BrandBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Admin Shield",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Slate900,
                    fontSize = 17.sp
                )
            }
        },
        actions = {
            // Language / RTL switch
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Slate100)
                    .border(1.dp, Slate200, RoundedCornerShape(20.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                IconButton(
                    onClick = onToggleLanguage,
                    modifier = Modifier
                        .size(34.dp)
                        .testTag("toggle_language_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Toggle Language",
                        tint = Slate700,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Refresh button
            IconButton(
                onClick = onRefresh,
                modifier = Modifier
                    .size(38.dp)
                    .testTag("refresh_dashboard_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = Slate600,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Logout button
            IconButton(
                onClick = onLogout,
                modifier = Modifier
                    .size(38.dp)
                    .testTag("logout_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = Slate600,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Slate900
        )
    )
}
private val Slate700 = Color(0xFF334155)
