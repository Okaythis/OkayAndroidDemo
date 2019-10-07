package com.ogieben.okaydemo.fcm

import java.io.Serializable

class NotificationDataContent(var tenantId: String?, val sessionId: String?): Serializable

