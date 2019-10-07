package com.ogieben.okaydemo.fcm

import java.io.Serializable


data class NotificationData(var type : NotificationType, var data: String): Serializable

