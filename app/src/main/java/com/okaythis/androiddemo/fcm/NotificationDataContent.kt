package com.okaythis.androiddemo.fcm

import java.io.Serializable

class NotificationDataContent(var tenantId: String?, val sessionId: String?): Serializable

