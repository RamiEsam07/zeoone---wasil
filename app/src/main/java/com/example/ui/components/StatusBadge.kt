package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DriverAvailability
import com.example.data.model.OrderStatus
import com.example.data.model.UserRole
import com.example.data.model.UserStatus
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandBlueContainer
import com.example.ui.theme.BrandBlueDark
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate700
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.StatusDangerContainer
import com.example.ui.theme.StatusDangerDark
import com.example.ui.theme.StatusInfo
import com.example.ui.theme.StatusInfoContainer
import com.example.ui.theme.StatusInfoDark
import com.example.ui.theme.StatusOrange
import com.example.ui.theme.StatusOrangeContainer
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.StatusSuccessDark
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningContainer
import com.example.ui.theme.StatusWarningDark
import com.example.ui.util.AdminStrings

@Composable
fun StatusBadge(
    text: String,
    containerColor: Color,
    contentColor: Color,
    borderColor: Color = containerColor,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(containerColor)
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun OrderStatusBadge(status: OrderStatus, isArabic: Boolean, modifier: Modifier = Modifier) {
    val text = AdminStrings.orderStatusLabel(status, isArabic)
    val (bg, content, border) = when (status) {
        OrderStatus.NEW -> Triple(BrandBlueContainer, BrandBlueDark, BrandBlue)
        OrderStatus.ACCEPTED -> Triple(StatusInfoContainer, StatusInfoDark, StatusInfo)
        OrderStatus.PREPARING -> Triple(StatusWarningContainer, StatusWarningDark, StatusWarning)
        OrderStatus.READY -> Triple(StatusOrangeContainer, StatusOrange, StatusOrange)
        OrderStatus.PICKED_UP -> Triple(StatusInfoContainer, StatusInfoDark, StatusInfo)
        OrderStatus.ON_THE_WAY -> Triple(BrandBlueContainer, BrandBlueDark, BrandBlue)
        OrderStatus.DELIVERED -> Triple(StatusSuccessContainer, StatusSuccessDark, StatusSuccess)
        OrderStatus.REJECTED -> Triple(StatusDangerContainer, StatusDangerDark, StatusDanger)
        OrderStatus.CANCELLED -> Triple(StatusDangerContainer, StatusDangerDark, StatusDanger)
    }
    StatusBadge(
        text = text,
        containerColor = bg,
        contentColor = content,
        borderColor = border.copy(alpha = 0.4f),
        modifier = modifier.testTag("order_status_badge_${status.name}")
    )
}

@Composable
fun DriverAvailabilityBadge(avail: DriverAvailability, isArabic: Boolean, modifier: Modifier = Modifier) {
    val text = AdminStrings.driverAvailabilityLabel(avail, isArabic)
    val (bg, content, border) = when (avail) {
        DriverAvailability.AVAILABLE -> Triple(StatusSuccessContainer, StatusSuccessDark, StatusSuccess)
        DriverAvailability.BUSY -> Triple(StatusWarningContainer, StatusWarningDark, StatusWarning)
        DriverAvailability.OFFLINE -> Triple(Slate200, Slate700, Slate200)
    }
    StatusBadge(
        text = text,
        containerColor = bg,
        contentColor = content,
        borderColor = border.copy(alpha = 0.4f),
        modifier = modifier.testTag("driver_avail_badge_${avail.name}")
    )
}

@Composable
fun UserRoleBadge(role: UserRole, isArabic: Boolean, modifier: Modifier = Modifier) {
    val text = AdminStrings.roleLabel(role, isArabic)
    val (bg, content) = when (role) {
        UserRole.ADMIN -> Pair(StatusDangerContainer, StatusDangerDark)
        UserRole.STORE -> Pair(StatusWarningContainer, StatusWarningDark)
        UserRole.DRIVER -> Pair(StatusInfoContainer, StatusInfoDark)
        UserRole.CUSTOMER -> Pair(BrandBlueContainer, BrandBlueDark)
    }
    StatusBadge(
        text = text,
        containerColor = bg,
        contentColor = content,
        modifier = modifier.testTag("user_role_badge_${role.name}")
    )
}

@Composable
fun UserStatusBadge(status: UserStatus, isArabic: Boolean, modifier: Modifier = Modifier) {
    val text = AdminStrings.userStatusLabel(status, isArabic)
    val (bg, content) = when (status) {
        UserStatus.ACTIVE -> Pair(StatusSuccessContainer, StatusSuccessDark)
        UserStatus.INACTIVE -> Pair(StatusDangerContainer, StatusDangerDark)
    }
    StatusBadge(
        text = text,
        containerColor = bg,
        contentColor = content,
        modifier = modifier.testTag("user_status_badge_${status.name}")
    )
}
