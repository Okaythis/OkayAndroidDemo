package com.okaythis.myokaycashapp.fcm

import java.io.Serializable


data class NotificationData(var type : NotificationType, var data: String): Serializable

