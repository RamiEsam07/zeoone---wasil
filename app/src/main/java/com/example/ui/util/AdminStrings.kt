package com.example.ui.util

import com.example.data.model.DriverAvailability
import com.example.data.model.OrderStatus
import com.example.data.model.UserRole
import com.example.data.model.UserStatus

object AdminStrings {

    fun tr(isArabic: Boolean, en: String, ar: String): String = if (isArabic) ar else en

    fun roleLabel(role: UserRole, isArabic: Boolean): String = when (role) {
        UserRole.ADMIN -> if (isArabic) "مدير النظام" else "ADMIN"
        UserRole.CUSTOMER -> if (isArabic) "عميل" else "CUSTOMER"
        UserRole.STORE -> if (isArabic) "متجر" else "STORE"
        UserRole.DRIVER -> if (isArabic) "سائق" else "DRIVER"
    }

    fun userStatusLabel(status: UserStatus, isArabic: Boolean): String = when (status) {
        UserStatus.ACTIVE -> if (isArabic) "نشط" else "ACTIVE"
        UserStatus.INACTIVE -> if (isArabic) "معطل" else "INACTIVE"
    }

    fun driverAvailabilityLabel(avail: DriverAvailability, isArabic: Boolean): String = when (avail) {
        DriverAvailability.AVAILABLE -> if (isArabic) "متاح" else "AVAILABLE"
        DriverAvailability.BUSY -> if (isArabic) "مشغول بتوصيل" else "BUSY"
        DriverAvailability.OFFLINE -> if (isArabic) "غير متصل" else "OFFLINE"
    }

    fun orderStatusLabel(status: OrderStatus, isArabic: Boolean): String = when (status) {
        OrderStatus.NEW -> if (isArabic) "طلب جديد" else "NEW"
        OrderStatus.ACCEPTED -> if (isArabic) "تم القبول" else "ACCEPTED"
        OrderStatus.PREPARING -> if (isArabic) "قيد التحضير" else "PREPARING"
        OrderStatus.READY -> if (isArabic) "جاهز للاستلام" else "READY"
        OrderStatus.PICKED_UP -> if (isArabic) "تم الاستلام" else "PICKED UP"
        OrderStatus.ON_THE_WAY -> if (isArabic) "في الطريق" else "ON THE WAY"
        OrderStatus.DELIVERED -> if (isArabic) "تم التوصيل" else "DELIVERED"
        OrderStatus.REJECTED -> if (isArabic) "مرفوض" else "REJECTED"
        OrderStatus.CANCELLED -> if (isArabic) "ملغي" else "CANCELLED"
    }
}
